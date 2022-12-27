package fr.eikasus.exitdotcom.services.implementations;

import fr.eikasus.exitdotcom.configuration.TracerProperties;
import fr.eikasus.exitdotcom.services.interfaces.Tracer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@EnableConfigurationProperties(TracerProperties.class)
@Service public class TracerImpl implements Tracer
{
	private final Logger logger;

	private final TracerProperties properties;

	public TracerImpl(@NotNull InjectionPoint ip, TracerProperties properties)
	{
		MethodParameter methodParameter = ip.getMethodParameter();
		Field field = ip.getField();
		Class<?> containerClass;

		if (methodParameter != null)
			containerClass = methodParameter.getContainingClass();
		else if (field != null) containerClass = field.getDeclaringClass();
		else throw new IllegalArgumentException();

		logger = LoggerFactory.getLogger(containerClass);

		this.properties = properties;

		String prefix = properties.getPrefix();
		String suffix = properties.getSuffix();

		properties.setPrefix((prefix == null) ? ("") : (prefix.concat(" ")));
		properties.setSuffix((suffix == null) ? ("") : (" ".concat(suffix)));
	}

	@Override public void info(String message)
	{
		if (properties.isActive())
			logger.info(String.format("%s%s%s", properties.getPrefix(), message, properties.getSuffix()));
	}
}
