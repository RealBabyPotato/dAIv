#!/usr/bin/env python
import os
# XML support
import xml.etree.ElementTree as ET

# Colours!
class col:
    ERR = "\x1b[1;31m"
    INFO = "\x1b[1;34m"
    CLEAR = "\x1b[0;0m"

# Set path to the dAIv repo's root directory
dir_path = os.path.dirname(os.path.realpath(__file__))
os.chdir(dir_path)

# Parse the POM to get the version
pom = ET.parse('./pom.xml')
root = pom.getroot()
version = root[0].text

# Get the current public IP address
ip = str(os.popen("curl https://ipinfo.io/ip").read())

# Package dAIv, return the Maven error code if it fails
print(col.INFO + "[ INFO ] Packaging dAIv..." + col.CLEAR)
status = os.system("mvn package --quiet")
if status != 0:
    print(col.ERR + "[ ERROR ] Packaging failed with error code " + str(status) + "." + col.CLEAR)
    exit()
print(col.INFO + "[ INFO ] Packaging complete!" + col.CLEAR)

# Run server
print(col.INFO + "[ INFO ] Starting server...")
# Find dependencies
dependencies = os.popen("mvn dependency:build-classpath | grep -e Dependencies --after-context 1 | tail -n 1").read().strip()
status = os.system("java -cp target/dAIv-" + version + ".jar:" + dependencies + " Main.java")
if status != 0:
    print(col.ERR + "[ ERROR ] Server start failed with error code " + str(status) + "." + col.CLEAR)
    exit()
print(col.INFO + "[ INFO ] Server running! Your IP address is " + ip + "." + col.CLEAR)

#TODO make a real console here and add support for adding users and the like from the console
while True:
    cmd = input(col.INFO + "dAIv version " + version + "> " + col.CLEAR)
    if cmd == "exit":
        exit()
