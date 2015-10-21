#! /bin/bash
docker run -d -p 8383:8080 -v `pwd`/gitbucket:/home/gitbucket/.gitbucket mikeneck/gitbucket
