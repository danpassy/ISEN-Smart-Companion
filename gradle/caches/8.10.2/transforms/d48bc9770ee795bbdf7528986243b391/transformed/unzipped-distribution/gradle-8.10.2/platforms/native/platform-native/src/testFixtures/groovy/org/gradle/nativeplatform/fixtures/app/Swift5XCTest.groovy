/*
 * Copyright 2019 the original author or authors.
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

package org.gradle.nativeplatform.fixtures.app

class Swift5XCTest extends XCTestSourceElement {
    Swift5XCTest(String projectName) {
        super(projectName)
    }

    @Override
    List<XCTestSourceFileElement> getTestSuites() {
        return [new XCTestSourceFileElement("Swift5Test") {
            @Override
            List<XCTestCaseElement> getTestCases() {
                return [testCase("testRawString",
                    '''let value = 42
                        let rawString = #"Raw string are ones with "quotes", backslash (\\), but can do special string interpolation (\\#(value))"#
                        XCTAssertEqual(rawString, "Raw string are ones with \\"quotes\\", backslash (\\\\), but can do special string interpolation (42)")''')]
            }
        }]
    }
}
