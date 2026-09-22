# llm-powered-utility

## Description:
This project is for learning purposes. We need to demonstrate our idea of what we learned initially regarding using LLM to create a simple application.
Apply tool calling for LLM to communicate with system. Provide appropriate prompts so the LLM can provide clear answer. Dabble with basics of
prompt engineering.

## Tech Stack Used:
1. JAVA
2. H2 Database
3. Data Jpa library
4. Spring Starter Open AI

### LLM used:
The LLM used in this practice is ***nvidia/nemotron-3-super-120b-a12b:free***, through open router. Although not very reliable it is good for initial phase of learning.
For advance we must use better model.

## What is actually done?
Currently only one task is done

### Financial message details generation through use of LLM***:
In this part we are invoking chat client, an spring ai library method to communicate with ***nvidia/nemotron-3-super-120b-a12b:free*** LLM.
Here we have integrated the openai dependency in our pom and used the libraries within in order to provide the user with an api that they can prompt into.
The prompt along with our system prompt where we strictly mention that only prompt related to financial message will be accepted is provided to LLM and get appropriate results.
For example, the input output formats are shown here:

***Input*** 
```text
Provide me the details of this message

{1:F01BANKUS33AXXX0000000000}
{2:I101BANKGB2LXXXXN}
{4:
:20:MT101REF00123
:28D:1/1
:50H:/123456789012
ACME CORP
1 MAIN STREET
NEW YORK
:30:260923
:21:TXN001
:32B:USD25000,00
:57A:DEUTDEFF
:59:/DE89370400440532013000
BENEFICIARY GMBH
HAUPTSTRASSE 10
FRANKFURT
:70:INVOICE 2026-0987
:71A:SHA
-}
```

***Output***

```text
This is an SWIFT MT101 message used for multiple payment instructions, often equivalent to pain.001 file. It contains a single transaction group with details for a credit transfer. 
 In this message, we can see:
1. Block 1 shows the sender's BIC as BANKUS33A.
2. Block 2 indicates an outward MT101 message to BANKGB2L.
3. Block 4 includes the transaction reference :20:MT101REF00123 and :28D:1/1.
4. :50H provides the ordering customer account and address (ACME CORP, New York).
5. :30 gives the date 2026-09-23.
6. :21 references the transaction ID TXN001.
7. :32B specifies the amount USD 25,000.00.
8. :57A indicates the receiving bank DEUTDEFF.
9. :59 provides the beneficiary account DE89370400440532013000 and address for BENEFICIARY GMBH in Frankfurt.
```

And there is response log as well where the logs are kept for each LLM response.

## Code Snippet:

***Dependencies included***
```xml
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-h2console</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webmvc</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.ai</groupId>
			<artifactId>spring-ai-starter-model-openai</artifactId>
			<exclusions>
				<exclusion>
					<groupId>io.swagger.core.v3</groupId>
					<artifactId>swagger-annotations</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
			<version>${spring-openai-doc.version}</version> <!-- check for the latest 2.x -->
		</dependency>
		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webmvc-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.ai</groupId>
				<artifactId>spring-ai-bom</artifactId>
				<version>${spring-ai.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
			<dependency>
				<groupId>org.springdoc</groupId>
				<artifactId>springdoc-openapi-bom</artifactId>
				<version>${spring-openai-doc.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>
```

***properties file***

```properties
spring.application.name=app

spring.ai.openai.api-key=${OPEN_ROUTER_TOKEN}
spring.ai.openai.base-url=https://openrouter.ai/api/v1
spring.ai.openai.chat.options.model=nvidia/nemotron-3-super-120b-a12b:free
spring.ai.openai.chat.options.max-tokens=1600

## H2 properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

***API Code***
```java
@RestController
@RequestMapping(value = "/api/v1/messaging")
@Tag(name = "Messaging", description = "LLM-backed chat endpoints for messaging")
public class MessagingController {

    private final LlmChatService service;

    public MessagingController(LlmChatService service) {
        this.service = service;
    }

    @Operation(
            summary = "Chat with the LLM powered messaging advisor",
            description = "Sends a plain-text prompt to the LLM and provide messaging description"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "PROVIDE LLM Response regarding financial message",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(type = "string")
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Missing or empty prompt", content = @Content),
            @ApiResponse(responseCode = "500", description = "LLM service error", content = @Content)
    })
    @PostMapping("/financeChat")
    public String normalChat(@RequestBody String userPrompt) {
        return service.response(userPrompt);
    }
}
```
***Service to call LLM***
```java
@Service
public class LlmChatService {

    private final Logger LOG = LoggerFactory.getLogger(LlmChatService.class);

    private final ChatClient chatClient;
    private final String SYSTEM_PROMPT;
    private final LoggingTools loggingTools;

    public LlmChatService(ChatClient.Builder chatClientBuilder
            , @Value("classpath:prompt.txt") Resource promptResource, LoggingTools loggingTools) throws IOException {
        this.chatClient = chatClientBuilder.build();
        this.SYSTEM_PROMPT = systemPrompt(promptResource);
        this.loggingTools = loggingTools;
    }

    public String response(String prompt) {
        try {
            LoggingDto response = chatClient.prompt().system(SYSTEM_PROMPT).user(prompt).call().entity(LoggingDto.class);
            if (response != null) {
                loggingTools.loggingTool(response);
                LOG.info("Response from LLM successfully logged" + response);
                return String.format("%s \n %s", response.getSummary(), response.getDetails());
            } else {
                LOG.error("Null response was propagated through LLM");
                throw new RuntimeException("Null response was propagated through LLM");
            }
        } catch (BadRequestException e) {
            LOG.error("status={} body={}", e.statusCode(), e.body(), e);
        } catch (OpenAIInvalidDataException e) {
            LOG.error("body={}", e.getMessage(), e);
        }
        return "Client/Server Error Encountered";
    }

    private String systemPrompt(Resource promptResource) throws IOException {
        assert promptResource != null;
        return promptResource.getContentAsString(StandardCharsets.UTF_8);
    }
}
```

## Conclusion:
This project is a learning project that helped learn the basics of spring ai and understand the compatibility of our code base with AI LLM models.
Note to remember, the currently used LLM is a 12B active parameter LLM model. The better model we use the better results can be acquired. 