#!/bin/bash +x

sudo apt-get update
sudo apt-get upgrade

# [+] install docker
sudo apt-get install docker.io --assume-yes
sudo usermod -aG docker "ubuntu"
# [-]