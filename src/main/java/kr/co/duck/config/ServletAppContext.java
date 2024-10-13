package kr.co.duck.config;

import java.util.Properties;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;

import kr.co.duck.beans.MemberBean;
import kr.co.duck.interceptor.CheckLoginInterceptor;
import kr.co.duck.interceptor.TopMenuInterceptor;
import kr.co.duck.repository.QuizMusicRepository;
import kr.co.duck.service.ManiaDBService;
import kr.co.duck.service.PlaylistManagementService;
import kr.co.duck.service.TopMenuService;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "kr.co.duck")
@MapperScan("kr.co.duck.mapper")
@MapperScan("kr.co.duck.dao")
@PropertySource("/WEB-INF/properties/db.properties")
@EnableJpaRepositories(basePackages = "kr.co.duck.repository")
public class ServletAppContext implements WebMvcConfigurer {

    @Value("${db.classname}")
    private String db_classname;

    @Value("${db.url}")
    private String db_url;

    @Value("${db.username}")
    private String db_username;

    @Value("${db.password}")
    private String db_password;

    @Autowired
    private TopMenuService topMenuService;

    @Resource(name = "loginMemberBean")
    private MemberBean loginMemberBean;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private QuizMusicRepository quizMusicRepository;

    // View Resolver 설정
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/", ".jsp");
    }

    // 정적 파일 경로 매핑
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("/resources/");
    }

    // 데이터베이스 접속 정보를 관리하는 Bean
    @Bean
    public BasicDataSource dataSource() {
        BasicDataSource source = new BasicDataSource();
        source.setDriverClassName(db_classname);
        source.setUrl(db_url);
        source.setUsername(db_username);
        source.setPassword(db_password);
        return source;
    }

    // SqlSessionFactory 설정
    @Bean
    public SqlSessionFactory factory(BasicDataSource source) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(source);
        return factoryBean.getObject();
    }

    // Interceptor 설정
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TopMenuInterceptor(topMenuService, loginMemberBean)).addPathPatterns("/**");
        registry.addInterceptor(new CheckLoginInterceptor(loginMemberBean))
                .addPathPatterns("/member/modify", "/member/logout", "/board/*")
                .excludePathPatterns("/board/main");
    }

    // Property 정보 설정
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    // 메시지 소스 설정
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource res = new ReloadableResourceBundleMessageSource();
        res.setDefaultEncoding("utf-8");
        res.setBasenames("/WEB-INF/properties/error_message");
        return res;
    }

    // 파일 업로드 처리
    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    // 추가 서비스 설정
    @Bean
    public PlaylistManagementService playlistManagementService() {
        return new PlaylistManagementService();
    }

    @Bean
    public ManiaDBService maniaDBService() {
        return new ManiaDBService();
    }

    // 이메일 설정
    @Bean("mailSender")
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("solduck0927@gmail.com");
        mailSender.setPassword("akgz uoqd ktpj kohw");
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setJavaMailProperties(getMailProperties());
        return mailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.connectiontimeout", 5000);
        return properties;
    }

    // JPA 설정
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("kr.co.duck.beans", "kr.co.duck.domain");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    // YouTube API 설정
    private static final GsonFactory GSON_FACTORY = GsonFactory.getDefaultInstance();

    @Bean
    public YouTube youtube() throws Exception {
        return new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), GSON_FACTORY, request -> {})
                .setApplicationName("YourApplicationName").build();
    }

   


}
