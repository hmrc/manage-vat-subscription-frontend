/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package config.features

import utils.TestUtil

class FeaturesSpec extends TestUtil {

  private val features = new Features

  override def beforeEach(): Unit = {
    super.beforeEach()
    features.simpleAuth(false)
    features.agentAccess(true)
  }

  "The Auth Features" should {

    "return its current state" in {
      features.simpleAuth() shouldBe false
      features.agentAccess() shouldBe true
    }

    "switch to a new state" in {
      features.simpleAuth(true)
      features.agentAccess(false)
      features.simpleAuth() shouldBe true
      features.agentAccess() shouldBe false
    }
  }
}