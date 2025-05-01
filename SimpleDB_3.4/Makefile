dirs:
	mkdir -p bin/server
	mkdir -p bin/client

compile_server: dirs
	javac -d bin/server simpledb/server/*

compile_clients: dirs
	javac -d bin/client simpleclient/SimpleIJ.java
	javac -d bin/client simpleclient/network/*
	javac -d bin/client simpleclient/embedded/*

build: compile_clients compile_server

server: compile_server
	cd bin/server; java simpledb.server.StartServer

setup:
	cd bin/client; java network.CreateStudentDB
	cd bin/client; java network.StudentMajor

client:
	cd bin/client; java SimpleIJ

