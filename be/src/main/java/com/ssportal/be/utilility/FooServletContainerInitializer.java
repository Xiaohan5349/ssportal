//package com.ssportal.be.utilility;
//
//import org.springframework.web.WebApplicationInitializer;
//
//import javax.servlet.ServletContainerInitializer;
//import javax.servlet.ServletContext;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.HandlesTypes;
//import java.util.Set;
//
//@HandlesTypes(WebApplicationInitializer.class)
//public class FooServletContainerInitializer implements ServletContainerInitializer {
//
//    @Override
//    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
//        for (Class<?> clazz : set) {
//            System.out.println ( clazz );
//            System.out.println ( clazz.getResource ( '/' + clazz.getName ().replace ( '.', '/' ) + ".class" ) );
//            System.out.println ( "----------------" );
//        }
//
//    }
//}
