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

@PackageScope
class ShellCommand {
    private def cmd

    private ShellCommand(exe) {
        cmd = [exe]
    }

    void args(... args) {
        cmd += args.flatten() - null
    }

    def exec() {
        println '$ ' + cmd.join(' ')
        new ProcessBuilder(cmd as String[]).redirectErrorStream(true).start()
    }

    static def exec(exe, Closure<Void> setup) {
        def shell = setup.delegate = new ShellCommand(exe)
        setup.call()
        shell.exec()
    }
}
