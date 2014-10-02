# -*- mode: ruby -*-
# vi: set ft=ruby :

#######
# Info:
# Add the following line to your /etc/hosts file without the #:
#
# trio.test 192.168.77.77
# 
# This will let you view the website by visiting trio.test in your web browser.
# Otherwise you can just view it by going to 192.168.77.77

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.box = "hashicorp/precise64"
  config.vm.synced_folder ".", "/src/trio-shows", type: "rsync",
    rsync__exclude: [".git/", "*.pyc", ".#*"]
  #config.vm.provision "file", source: ".vagrant/bashrc", destination: "/home/vagrant/.bashrc"
  config.vm.provision "shell", path: ".vagrant/provision.sh"
  config.vm.hostname = "trio.test"
  config.vm.network "private_network", ip: "192.168.77.77"
end
