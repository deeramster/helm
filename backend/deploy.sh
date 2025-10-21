#!/bin/bash

docker --context remote ps -a | grep 'sausage-store-backend-blue' | awk '{print $2}' | awk -F: '{print $NF}' > VERSION_BLUE
docker --context remote ps -a | grep 'sausage-store-backend-green' | awk '{print $2}' | awk -F: '{print $NF}' > VERSION_GREEN

BLUE_MAJOR=$(awk -F. '{print $1}' VERSION_BLUE)
BLUE_MINOR=$(awk -F. '{print $2}' VERSION_BLUE)
BLUE_BUILD=$(awk -F. '{print $3}' VERSION_BLUE)

GREEN_MAJOR=$(awk -F. '{print $1}' VERSION_GREEN)
GREEN_MINOR=$(awk -F. '{print $2}' VERSION_GREEN)
GREEN_BUILD=$(awk -F. '{print $3}' VERSION_GREEN)

if [[ "$BLUE_MAJOR" -gt "$GREEN_MAJOR" ]]; then
 ACTIVE_CONTAINER=sausage-store-backend-blue
 VERSION_ACTIVE=$(cat VERSION_BLUE)
 echo "sausage-store-backend-blue активный, версия: $VERSION_ACTIVE"
elif [[ "$BLUE_MAJOR" -eq "$GREEN_MAJOR" && "$BLUE_MINOR" -gt "$GREEN_MINOR" ]]; then
 ACTIVE_CONTAINER=sausage-store-backend-blue
 VERSION_ACTIVE=$(cat VERSION_BLUE)
 echo "sausage-store-backend-blue активный, версия: $VERSION_ACTIVE"
elif [[ "$BLUE_MAJOR" -eq "$GREEN_MAJOR" && "$BLUE_MINOR" -eq "$GREEN_MINOR" && "$BLUE_BUILD" -gt "$GREEN_BUILD" ]]; then
 ACTIVE_CONTAINER=sausage-store-backend-blue
 VERSION_ACTIVE=$(cat VERSION_BLUE)
 echo "sausage-store-backend-blue активный, версия: $VERSION_ACTIVE"
elif [[ "$GREEN_MAJOR" -gt "$BLUE_MAJOR" ]]; then
 ACTIVE_CONTAINER=sausage-store-backend-green
 VERSION_ACTIVE=$(cat VERSION_GREEN)
 echo "sausage-store-backend-green активный, версия: $VERSION_ACTIVE"
elif [[ "$GREEN_MAJOR" -eq "$BLUE_MAJOR" && "$GREEN_MINOR" -gt "$BLUE_MINOR" ]]; then
 ACTIVE_CONTAINER=sausage-store-backend-green
 VERSION_ACTIVE=$(cat VERSION_GREEN)
 echo "sausage-store-backend-green активный, версия: $VERSION_ACTIVE"
elif [[ "$GREEN_MAJOR" -eq "$BLUE_MAJOR" && "$GREEN_MINOR" -eq "$BLUE_MINOR" && "$GREEN_BUILD" -gt "$BLUE_BUILD" ]]; then
 ACTIVE_CONTAINER=sausage-store-backend-green
 VERSION_ACTIVE=$(cat VERSION_GREEN)
 echo "sausage-store-backend-green активный, версия: $VERSION_ACTIVE"
else
 ACTIVE_CONTAINER=sausage-store-backend-blue
 VERSION_ACTIVE=$(cat VERSION_BLUE)
 echo "Версии sausage-store-backend-blue и sausage-store-backend-green совпадают, выключаем backend-blue"
fi

if [[ "$ACTIVE_CONTAINER" == "sausage-store-backend-blue" ]]; then
 CONTAINER_TO_STOP=$(docker --context remote ps -a | grep 'sausage-store-backend-green' | awk '{print $1}')
 OLD_CONTAINER=$(docker --context remote ps -a | grep 'sausage-store-backend-blue' | awk '{print $1}')
 UPDATE_CONTAINER="backend-green"
elif [[ "$ACTIVE_CONTAINER" == "sausage-store-backend-green" ]]; then
 CONTAINER_TO_STOP=$(docker --context remote ps -a | grep 'sausage-store-backend-blue' | awk '{print $1}')
 OLD_CONTAINER=$(docker --context remote ps -a | grep 'sausage-store-backend-green' | awk '{print $1}')
 UPDATE_CONTAINER="backend-blue"
else
 echo "Некорректное значение переменной ACTIVE_CONTAINER: $ACTIVE_CONTAINER"
 exit 1
fi

docker --context remote rm -f $CONTAINER_TO_STOP
docker --context remote compose --env-file .env up $UPDATE_CONTAINER -d --pull "always" --force-recreate 
sleep 30

VALUE=$(docker --context remote ps -a | grep $ACTIVE_CONTAINER)
STATUS=$(awk '{print substr($0, match($0, "\\([^)]+\\)"), RLENGTH)}' <<< $VALUE)


if [[ "$STATUS" == "(healthy)" ]]; then
 echo "Статус 'healthy', новый контейнер успешно запущен, останавливаем старый"
 docker --context remote rm -f $OLD_CONTAINER
else
 echo "Ошибка: новый контейнер в статусе $STATUS, не останавливаем старый"
fi
