package org.antonakospanos.iot.atlas.web.support;

import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MutableHttpServletRequest extends HttpServletRequestWrapper {

	private byte[] payload;

	public MutableHttpServletRequest(HttpServletRequest request) throws IOException {
		super(request);
		// FIXME: Throws IOException on files > 1MB!
		// org.apache.tomcat.util.http.fileupload.FileUploadBase$SizeLimitExceededException should only handle long payloads (~80MB)
		this.payload = StreamUtils.copyToByteArray(request.getInputStream());
	}

	@Override
	public ServletInputStream getInputStream () {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.payload);

		return new DelegatingServletInputStream(byteArrayInputStream);
	}
}