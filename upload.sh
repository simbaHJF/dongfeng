#!/usr/bin/env bash

gatewayDir="/home/work/qinxc"
gatewayHost="192.168.1.69"
gatewayUser="work"
gatewayPass="bzp_@eYix9b"
gatewayPort="22"

databusDir="/data/rcd_deploy/jinnang/"
databusHost="47.94.99.134"
databusUser="rcd_deploy"
databusPort="13122"

pixiuUser="work"
pixiuPort="13122"

pixiuHost=${1}
pixiuDir=${2}
warFile=${3}
currentDir=$(pwd)
fileDir=${currentDir}/${4}


echo $pixiuHost ":" $pixiuDir



/usr/bin/expect<<EOF
	set timeout 600

	spawn rsync -avv --progress -c -e "ssh -p ${gatewayPort}" ${fileDir}/${warFile} ${gatewayUser}@${gatewayHost}:${gatewayDir}
	expect 	{
        "*(yes/no)*" {
            send "yes\r"
            expect "*assword" { send "${gatewayPass}\r"}
            expect 	"*100%*"
        }
	    "*assword*" {
            send 	"${gatewayPass}\r"
            expect 	"*100%*"
        }
        "*total size*" {
            send 	"\r"
        }
    }

	spawn ssh -p ${gatewayPort} ${gatewayUser}@${gatewayHost}
	expect 	{
	    "*assword*" {
            send 	"${gatewayPass}\r"
            expect	"*Last login*"
        }
        "*Last login*" {
            send 	"\r"
        }
    }

	send 	"cd ${gatewayDir}\r"
	send    "rsync -avv --progress -c -e \"ssh -p ${databusPort}\" ${warFile} ${databusUser}@${databusHost}:${databusDir}\r"
	expect 	{
	    "*total size*" {
            send 	"\r"
        }
	}
	send	"ssh -p ${databusPort} ${databusUser}@${databusHost}\r"
	expect	"*login*"

	send 	"cd ${databusDir}\r"
	send    "rsync -avv --progress -c -e \"ssh -p ${pixiuPort}\" ${warFile} ${pixiuUser}@${pixiuHost}:${pixiuDir}\r"
	expect 	{
	    "*total size*" {
            send 	"\r"
        }
	}
EOF
