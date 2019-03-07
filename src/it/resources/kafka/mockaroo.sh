#!/usr/bin/env bash

while [ 1 -eq 1 ]
  do curl "https://api.mockaroo.com/api/ff3df5f0?count=1&key=adc016a0" | \
    awk '{print $$0;system("sleep 0.5");}' | kafka-console-producer --broker-list localhost:9092 --topic mockaroo
  done