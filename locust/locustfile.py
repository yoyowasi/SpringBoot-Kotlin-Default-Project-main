import random
import uuid
from locust import HttpUser, task, between
from faker import Faker
from config import (
    JOB_CREATE_PROBABILITY,
    REVIEW_CREATE_PROB,
    REVIEW_COMMENT_PROB,
    REVIEW_LIKE_PROB,
    FESTIVAL_LIKE_PROB,
    JOB_APPLY_PROB,
    JOB_CANCEL_PROB,
)

fake = Faker("ko_KR")

SIGNUP_ENABLED = True

EXISTING_USERS = [f"test_{i}" for i in range(1, 30121)]
FIXED_PASSWORD = "1234"


def random_id():
    return "u_" + uuid.uuid4().hex[:10]


def random_pw():
    return "p_" + uuid.uuid4().hex[:10]


def random_text(words=15):
    return fake.sentence(nb_words=words)


class FestivalUser(HttpUser):
    wait_time = between(1, 2)

    def on_start(self):
        # 유저별 상태 저장 공간
        self.reviewed_festivals = set()
        self.applied_jobs = set()

        if SIGNUP_ENABLED:
            self.user_id = random_id()
            self.user_pw = random_pw()

            self.signup()
            self.login()
        else:
            self.user_id = random.choice(EXISTING_USERS)
            self.user_pw = FIXED_PASSWORD

            self.login()

    def signup(self):
        payload = {
            "id": self.user_id,
            "pw": self.user_pw,
            "email": f"{uuid.uuid4().hex[:6]}@test.com",
            "name": fake.name(),
            "termsOfService": True,
            "privacyPolicy": True,
            "alertPolicy": True
        }
        self.client.post("/app/user/sign-up", json=payload)

    def login(self):
        payload = {"id": self.user_id, "pw": self.user_pw}
        res = self.client.post("/app/user/login", json=payload)

        try:
            self.token = res.json().get("accessToken")
        except:
            self.token = None

    def headers(self):
        if not self.token:
            return {}
        return {"Authorization": f"Bearer {self.token}"}

    #---------------------------------------------------
    # FESTIVAL LIST + LIKE
    #---------------------------------------------------
    @task(3)
    def festival_list_task(self):
        res = self.client.get("/app/festival/list?page=1", headers=self.headers())
        if res.status_code not in (200, 201):
            return

        festivals = res.json()
        if not festivals:
            return

        selected = random.choice(festivals)
        self.festivalId = selected["id"]

        # 좋아요 처리
        if random.random() < FESTIVAL_LIKE_PROB:
            self.client.post(
                f"/app/festival/{self.festivalId}/like",
                headers=self.headers()
            )

    #---------------------------------------------------
    # REVIEW CREATE + LIKE + COMMENT
    #---------------------------------------------------
    @task(2)
    def festival_review_flow(self):
        if not hasattr(self, "festivalId"):
            return

        # 이미 리뷰한 축제면 스킵
        if self.festivalId in self.reviewed_festivals:
            return

        if random.random() < REVIEW_CREATE_PROB:
            payload = {}
            payload["rating"] = random.randint(1, 5)
            payload["content"] = random_text()
            payload["type"] = random.choice(["REVIEW", "TIP", "MATE"])

            res = self.client.post(
                f"/app/festival/{self.festivalId}/reviews",
                json=payload,
                headers=self.headers()
            )

            if res.status_code in (200, 201):
                # 성공한 축제는 리뷰 리스트에 저장
                self.reviewed_festivals.add(self.festivalId)

                review_id = res.json()["id"]

                # 리뷰 좋아요
                if random.random() < REVIEW_LIKE_PROB:
                    self.client.post(
                        f"/app/festival/reviews/{review_id}/like",
                        headers=self.headers()
                    )

                # 댓글 작성
                if random.random() < REVIEW_COMMENT_PROB:
                    comment_payload = {"content": random_text(10)}
                    self.client.post(
                        f"/app/festival/reviews/{review_id}/comments",
                        json=comment_payload,
                        headers=self.headers()
                    )

    #---------------------------------------------------
    # JOB LIST + APPLY + CANCEL
    #---------------------------------------------------
    @task(2)
    def job_flow(self):
        res = self.client.get("/app/festival/job/list?page=1", headers=self.headers())
        if res.status_code != 200:
            return

        jobs = res.json()
        if not jobs:
            return

        job = random.choice(jobs)
        jobId = job["jobId"]

        # 이미 지원한 job이면 스킵
        if jobId in self.applied_jobs:
            return

        if random.random() < JOB_APPLY_PROB:
            apply_payload = {
                "name": fake.name(),
                "gender": random.choice(["M", "F"]),
                "age": random.randint(20, 60),
                "location": fake.city(),
                "introduction": random_text(20),
                "career": random_text(20)
            }

            res2 = self.client.post(
                f"/app/festival/job/{jobId}/apply",
                json=apply_payload,
                headers=self.headers()
            )

            if res2.status_code in (200, 201):
                self.applied_jobs.add(jobId)

                # 취소 확률
                if random.random() < JOB_CANCEL_PROB:
                    self.client.delete(
                        f"/app/festival/job/{jobId}/apply",
                        headers=self.headers()
                    )
                    self.applied_jobs.discard(jobId)

    #---------------------------------------------------
    # RARE: JOB CREATE
    #---------------------------------------------------
    @task(1)
    def job_creation(self):
        if random.random() > JOB_CREATE_PROBABILITY:
            return

        res = self.client.get("/app/festival/list?page=1", headers=self.headers())
        if res.status_code != 200:
            return

        festivals = res.json()
        if not festivals:
            return

        festivalId = random.choice(festivals)["id"]

        payload = {
            "title": random_text(),
            "shortDesc": random_text(),
            "detailDesc": random_text(20),
            "hourlyPay": random.randint(10000, 20000),
            "workTime": "10:00-18:00",
            "workPeriod": "2025-12-10~2025-12-20",
            "preference": ["친절함", "성실함"],
            "isCertified": random.choice([True, False]),
            "deadline": "2025-12-30"
        }

        self.client.post(
            f"/app/festival/job/{festivalId}/create",
            json=payload,
            headers=self.headers()
        )
