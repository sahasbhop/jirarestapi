#!/bin/bash

source="${BASH_SOURCE[0]}"
dirname="${source%/*.sh}"

cd "$dirname"

java -jar app.jar $1 $2 $3 $4 $5 $6 $7