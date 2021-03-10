cmake_minimum_required(VERSION 3.6)

set(target glmodel)
project(${target} CXX)

message(STATUS "ANDROID_ABI=${ANDROID_ABI}")

file(GLOB srcs *.cpp *.c)
file(GLOB hdrs *.hpp *.h)

include_directories(".")
add_library(${target} SHARED ${srcs} ${hdrs})
