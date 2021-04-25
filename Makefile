run: run-infra
	docker-compose -f ./environments/dev/docker-compose-app.yml up --build

run-infra:
	docker-compose -f ./environments/dev/docker-compose-infra.yml up -d

stop-infra:
	docker-compose -f ./environments/dev/docker-compose-infra.yml stop