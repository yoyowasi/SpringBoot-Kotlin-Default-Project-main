from locust import HttpUser, task, between

ADMIN_ID = "minq"
ADMIN_PW = "minq"

class AdminUser(HttpUser):
    wait_time = between(0.5, 1)  # 부하 강하게 넣고 싶으면 줄여라.

    def on_start(self):
        """admin 계정 로그인 및 토큰 획득"""
        payload = {
            "id": ADMIN_ID,
            "pw": ADMIN_PW
        }

        res = self.client.post("/app/user/login", json=payload)

        try:
            self.token = res.json()["accessToken"]
            print("[Admin] Login success. Token issued.")
        except:
            self.token = None
            print("[Admin] Login FAILED !!!!!")

    def headers(self):
        if not self.token:
            return {}
        return {"Authorization": f"Bearer {self.token}"}

    @task
    def cpu_burn_test(self):
        """CPU 부하 테스트 API 반복 호출"""
        seconds = 2  # 원하는 부하 시간
        self.client.get(
            f"/app/dev/cpu-burn/{seconds}",
            headers=self.headers()
        )
