package com.licious.oms.docx;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Licious Order Management System",
                version = "1.0.0",
                description = """
                The Licious Order Management System (LOMS) is a comprehensive platform designed to streamline and \n
                optimize the order processing workflow for Licious, a leading online meat and seafood delivery service.\n 
                LOMS provides a user-friendly interface for managing orders from the point of placement to delivery.
                """,
                termsOfService = "terms of service",
                contact = @Contact(
                        name = "Ramakrishna Janapureddy",
                        email = "developer.raakhi005@gmail.com"
                ),
                license = @License(
                        name = "Apache License 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        )
)
public class OpenAPIDocxConfig {

}
