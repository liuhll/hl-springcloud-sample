package com.liuhll.hl.auth.client;

import com.liuhll.hl.auth.client.conf.HlOnlyUserAuthConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(HlOnlyUserAuthConfiguration.class)
public @interface EnableHlOnlyUserAuthClient {


}
