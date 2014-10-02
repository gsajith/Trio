#!/bin/bash

wget https://bootstrap.pypa.io/get-pip.py -O /tmp/get-pip.py
sudo python /tmp/get-pip.py
rm /tmp/get-pip.py

sudo apt-get update
sudo apt-get install -y \
    vim git build-essential python-dev libffi-dev libxml2-dev libxslt-dev

sudo cp /home/vagrant/.bashrc /root/.bashrc
