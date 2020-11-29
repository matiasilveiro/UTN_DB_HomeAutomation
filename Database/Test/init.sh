#!/bin/bash

echo "\n\033[32m>>>> Iniciando proceso MySQL\033[0m"
sudo /etc/init.d/mysql start

echo "\n\033[32m>>>> Generando base de datos\033[0m"
sudo mysql < Automastic_MySQL.sql

echo "\n\033[32m>>>> Ingresando a MySQL\033[0m"
sudo mysql