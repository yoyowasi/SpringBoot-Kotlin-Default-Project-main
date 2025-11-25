from locust import HttpUser, task, between
import random
import uuid
from faker import Faker

fake = Faker("ko_KR")

def random_text(words=15):
    return fake.sentence(nb_words=words)

class LoginOnly(HttpUser):
    host = "http://localhost:8080"
    wait_time = between(1, 2)

    def on_start(self):
        idx = random.randint(1, 30000)
        self.user_id = f"test_{idx}"
        self.user_pw = "1234"
        self.token = None  # <<< 여기 추가

    @task
    def login(self):
        payload = {
            "id": self.user_id,
            "pw": self.user_pw
        }
        res = self.client.post("/app/user/login", json=payload)
        try:
            self.token = res.json().get("accessToken")
        except Exception:
            self.token = None

    def headers(self):
        if not self.token:
            return {}
        return {"Authorization": f"Bearer {self.token}"}

    @task
    def festival_review_flow(self):
        if not self.token:
            return

        # 리뷰 작성
        if random.random() < 0.3:
            payload = {
                "rating": random.randint(1, 5),
                "content": random_text(),
                "type": random.choice(["REVIEW", "TIP", "MATE"])
            }
            print("Review payload:", payload)

            res = self.client.post(
                f"/app/festival/6942/reviews",
                json=payload,
                headers={**self.headers(), "Content-Type": "application/json"}
            )

            if res.status_code not in (200, 201):
                return
