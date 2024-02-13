/*
 * Copyright 2015-2022 Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

const AMSTERDAM_EMAILAPI_PLUGIN_LOGO_BASE64 =
'data:image/png;base64,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';

export {AMSTERDAM_EMAILAPI_PLUGIN_LOGO_BASE64};