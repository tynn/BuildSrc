/*
 * Copyright (C) 2017 volders GmbH with <3 in Berlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package berlin.volders.buildsrc.monkey

import groovy.transform.PackageScope
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Task
import org.gradle.api.tasks.TaskAction

import static java.lang.Math.min;

class AndroidMonkeyTask extends DefaultTask {
    long count

    String[] categories

    boolean ignoreCrashes
    boolean ignoreTimeouts
    boolean ignoreSecurityExceptions
    boolean monitorNativeCrashes
    boolean ignoreNativeCrashes
    boolean killProcessAfterError
    boolean hprof
    int pctTouch = -1
    int pctmotion = -1
    int pctTrackball = -1
    int pctNav = -1
    int pctSyskeys = -1
    int pctAppswitch = -1
    int pctMajornav = -1
    int pctFlip = -1
    int pctAnyevent = -1
    int pctPinchzoom = -1
    int pctPermission = -1
    boolean dbgNoEvents
    Long seed
    int verbose
    long throttle
    boolean randomizeThrottle
    boolean bugreport
    boolean permissionTargetSystem

    @TaskAction
    def runMonkey() {
        def applicationIds = project.android.applicationVariants
                .grep { it.install in dependsOnTasks }
                .collect { it.applicationId }
                .unique() - null

        def proc = ShellCommand.exec(project.android.adbExe) {
            args '-e', 'shell', 'monkey'

            args applicationIds.collect {
                ['-p', it]
            }.flatten()

            args categories.collect {
                ['-c', it]
            }.flatten()

            args(
                    ignoreCrashes ? '--ignore-crashes' : null,
                    ignoreTimeouts ? '--ignore-timeouts' : null,
                    ignoreSecurityExceptions ? '--ignore-security-exceptions' : null,
                    monitorNativeCrashes ? '--monitor-native-crashes' : null,
                    ignoreNativeCrashes ? '--ignore-native-crashes' : null,
                    killProcessAfterError ? '--kill-process-after-error' : null,
                    hprof ? '--hprof' : null,
                    pctTouch < 0 ? null : ['--pct-touch', min(pctTouch, 100)],
                    pctmotion < 0 ? null : ['--pct-motion', min(pctmotion, 100)],
                    pctTrackball < 0 ? null : ['--pct-trackball', min(pctTrackball, 100)],
                    pctNav < 0 ? null : ['--pct-nav', min(pctNav, 100)],
                    pctSyskeys < 0 ? null : ['--pct-syskeys', min(pctSyskeys, 100)],
                    pctAppswitch < 0 ? null : ['--pct-appswitch', min(pctAppswitch, 100)],
                    pctMajornav < 0 ? null : ['--pct-majornav', min(pctMajornav, 100)],
                    pctFlip < 0 ? null : ['--pct-flip', min(pctFlip, 100)],
                    pctAnyevent < 0 ? null : ['--pct-anyevent', min(pctAnyevent, 100)],
                    pctPinchzoom < 0 ? null : ['--pct-pinchzoom', min(pctPinchzoom, 100)],
                    pctPermission < 0 ? null : ['--pct-permission', min(pctPermission, 100)],
                    dbgNoEvents ? '--dbg-no-events' : null,
                    seed == null ? null : ['-s', seed],
                    verbose > 0 ? ['-v'] * verbose : null,
                    throttle > 0 ? ['--throttle', throttle] : null,
                    randomizeThrottle ? '--randomize-throttle' : null,
                    bugreport ? '--bugreport' : null,
                    permissionTargetSystem ? '--permission-target-system' : null,
            )

            args count <= 0 ? 1000 : count
        }

        proc.inputStream.eachLine { println it }
        if (proc.waitFor() != 0) {
            throw new GradleException("Monkey failed with ${proc.exitValue()}")
        }
    }

    void setC(String... c) {
        categories = c
    }

    def getC() {
        categories
    }

    void setS(s) {
        seed = s
    }

    def getS() {
        seed
    }

    void setV(v) {
        verbose = v
    }

    def getV() {
        verbose
    }

    @PackageScope
    def getDependsOnTasks() {
        dependsOn.collect {
            if (it in Task) {
                it
            } else try {
                project.tasks[it]
            } catch (e) {
                null
            }
        }.grep()
    }
}
