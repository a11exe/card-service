# Card service
Springboot / Postgresql /Active MQ / Rest tempale

## Install

    git clone https://github.com/a11exe/card-service
    
## Run

### start postman mock server

    import mock data from ./postman-mock-server/cards-fact.postman_collection.json
    
### start postgresql database and active mq in docker

    sh run.sh (from card-service folder)
        
### start application    

    mvn clean install -DskipTests=true spring-boot:run (from card-service folder)
    
### check rest api
    
    curl --location --request GET 'http://localhost:8080/cards/user/1'
    
### stop postgresql database and active mq in docker

    sh stop.sh (from card-service folder)

    