LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

#OPENCV_INSTALL_MODULES:=off
#OPENCV_LIB_TYPE:=SHARED

LOCAL_SRC_FILES  := gl_model.cpp
LOCAL_C_INCLUDES += $(LOCAL_PATH)
LOCAL_LDLIBS     += -llog -ldl

LOCAL_MODULE     := glmodel

include $(BUILD_SHARED_LIBRARY)
