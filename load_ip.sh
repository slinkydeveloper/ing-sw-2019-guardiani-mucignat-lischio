#!/usr/bin/env bash

function get_ip() {
    unameOut="$(uname -s)"
    case "${unameOut}" in
        Linux*)     machine=Linux;;
        Darwin*)    machine=Mac;;
        CYGWIN*)    machine=Cygwin;;
        MINGW*)     machine=MinGw;;
        *)          machine="UNKNOWN:${unameOut}"
    esac
    if [[ $machine == "Linux" ]]; then
        discovered_ip=$(hostname -I | awk '{print $1}')
    else
        discovered_ip=$(ipconfig getifaddr $(networksetup -listallhardwareports | awk '/Hardware Port: Wi-Fi/{getline; print $2}'))
    fi
}
