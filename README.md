BuildSrc
========
[![Build][1]][2]
[![Release][3]][4]

*BuildSrc* is a collection of Gradle plugins for Android development.


Usage
-----


Installation
------------

Add [JitPack][4] to your build repositories and *BuildSrc* to the classpath

    buildscript {
        repositories {
            maven { url 'https://jitpack.io' }
        }
        dependencies {
            classpath "berlin.volders:buildSrc:$buildSrcVersion"
        }
    }


License
-------

    Copyright (C) 2017 volders GmbH with <3 in Berlin

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


  [1]: https://travis-ci.org/volders/BuildSrc.svg?branch=master
  [2]: https://travis-ci.org/volders/BuildSrc
  [3]: https://jitpack.io/v/berlin.volders/buildSrc.svg
  [4]: https://jitpack.io/#berlin.volders/buildSrc
