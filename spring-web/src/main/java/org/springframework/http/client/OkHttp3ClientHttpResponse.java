/*
 * Copyright 2002-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.http.client;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;
import okhttp3.ResponseBody;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * {@link ClientHttpResponse} implementation based on OkHttp 3.x.
 *
 * @author Luciano Leggieri
 * @author Arjen Poutsma
 * @author Roy Clarkson
 * @since 4.3
 * @deprecated since 6.1, in favor of other HTTP client libraries;
 * scheduled for removal in 7.0
 */
@Deprecated(since = "6.1", forRemoval = true)
class OkHttp3ClientHttpResponse implements ClientHttpResponse {

	private final Response response;

	@Nullable
	private volatile HttpHeaders headers;


	public OkHttp3ClientHttpResponse(Response response) {
		Assert.notNull(response, "Response must not be null");
		this.response = response;
	}


	@Override
	public HttpStatusCode getStatusCode() throws IOException {
		return HttpStatusCode.valueOf(this.response.code());
	}

	@Override
	public String getStatusText() {
		return this.response.message();
	}

	@Override
	public InputStream getBody() throws IOException {
		ResponseBody body = this.response.body();
		return (body != null ? body.byteStream() : InputStream.nullInputStream());
	}

	@Override
	public HttpHeaders getHeaders() {
		HttpHeaders headers = this.headers;
		if (headers == null) {
			headers = new HttpHeaders();
			for (String headerName : this.response.headers().names()) {
				for (String headerValue : this.response.headers(headerName)) {
					headers.add(headerName, headerValue);
				}
			}
			this.headers = headers;
		}
		return headers;
	}

	@Override
	public void close() {
		ResponseBody body = this.response.body();
		if (body != null) {
			body.close();
		}
	}

}
