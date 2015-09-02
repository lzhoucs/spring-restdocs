package org.springframework.restdocs.operation.preprocess;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.restdocs.hypermedia.Link;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Tests for {@link LinkMaskingContentModifier}
 * 
 * @author Andy Wilkinson
 *
 */
public class LinkMaskingContentModifierTests {

	private final ContentModifier contentModifier = new LinkMaskingContentModifier();

	private final Link[] links = new Link[] { new Link("a", "alpha"),
			new Link("b", "bravo") };

	private final Link[] maskedLinks = new Link[] { new Link("a", "..."),
			new Link("b", "...") };

	@Test
	public void halLinksAreMasked() throws Exception {
		assertThat(this.contentModifier.modifyContent(halPayloadWithLinks(this.links)),
				is(equalTo(halPayloadWithLinks(this.maskedLinks))));
	}

	@Test
	public void formattedHalLinksAreMasked() throws Exception {
		assertThat(
				this.contentModifier
						.modifyContent(formattedHalPayloadWithLinks(this.links)),
				is(equalTo(formattedHalPayloadWithLinks(this.maskedLinks))));
	}

	@Test
	public void atomLinksAreMasked() throws Exception {
		assertThat(this.contentModifier.modifyContent(atomPayloadWithLinks(this.links)),
				is(equalTo(atomPayloadWithLinks(this.maskedLinks))));
	}

	@Test
	public void formattedAtomLinksAreMasked() throws Exception {
		assertThat(
				this.contentModifier
						.modifyContent(formattedAtomPayloadWithLinks(this.links)),
				is(equalTo(formattedAtomPayloadWithLinks(this.maskedLinks))));
	}

	@Test
	public void maskCanBeCustomized() throws Exception {
		assertThat(
				new LinkMaskingContentModifier("custom")
						.modifyContent(formattedAtomPayloadWithLinks(this.links)),
				is(equalTo(formattedAtomPayloadWithLinks(new Link("a", "custom"),
						new Link("b", "custom")))));
	}

	private byte[] atomPayloadWithLinks(Link... links) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsBytes(createAtomPayload(links));
	}

	private byte[] formattedAtomPayloadWithLinks(Link... links)
			throws JsonProcessingException {
		return new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true)
				.writeValueAsBytes(createAtomPayload(links));
	}

	private AtomPayload createAtomPayload(Link... links) {
		AtomPayload payload = new AtomPayload();
		payload.setLinks(Arrays.asList(links));
		return payload;
	}

	private byte[] halPayloadWithLinks(Link... links) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsBytes(createHalPayload(links));
	}

	private byte[] formattedHalPayloadWithLinks(Link... links)
			throws JsonProcessingException {
		return new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true)
				.writeValueAsBytes(createHalPayload(links));
	}

	private HalPayload createHalPayload(Link... links) {
		HalPayload payload = new HalPayload();
		Map<String, Object> linksMap = new LinkedHashMap<>();
		for (Link link : links) {
			Map<String, String> linkMap = new HashMap<>();
			linkMap.put("href", link.getHref());
			linksMap.put(link.getRel(), linkMap);
		}
		payload.setLinks(linksMap);
		return payload;
	}

	static final class AtomPayload {

		private List<Link> links;

		public void setLinks(List<Link> links) {
			this.links = links;
		}

		public List<Link> getLinks() {
			return this.links;
		}

	}

	static final class HalPayload {

		private Map<String, Object> links;

		@JsonProperty("_links")
		public Map<String, Object> getLinks() {
			return this.links;
		}

		public void setLinks(Map<String, Object> links) {
			this.links = links;
		}

	}
}