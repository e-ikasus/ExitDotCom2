package fr.eikasus.exitdotcom.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("tracer")
@Getter @Setter
public class TracerProperties
{
	private boolean active;

	private String prefix;

	private String suffix;
}
