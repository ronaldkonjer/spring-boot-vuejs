package eu.comparegroup.partnernet.controller

import kotlin.eu.comparegroup.partnernet.PartnernetSpringBootVuejsApplication
import kotlin.eu.comparegroup.partnernet.domain.User
import io.restassured.RestAssured
import io.restassured.RestAssured.`when`
import io.restassured.RestAssured.given
import org.apache.http.HttpStatus
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.eu.comparegroup.partnernet.controller.BackendController

@ExtendWith(SpringExtension::class)
@SpringBootTest(
	classes = [PartnernetSpringBootVuejsApplication::class],
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class BackendControllerTest {

	@LocalServerPort
	private val port = 0

	@BeforeEach
	fun init() {
		RestAssured.baseURI = "http://localhost"
		RestAssured.port = port
	}

	@Test
	fun saysHello() {
		`when`()
			.get("/api/hello")
			.then()
			.statusCode(HttpStatus.SC_OK)
			.assertThat()
			.body(Matchers.`is`(Matchers.equalTo(BackendController.HELLO_TEXT)))
	}

	@Test
	fun addNewUserAndRetrieveItBack() {
//		val norbertSiegmund = User("Norbert", "Siegmund")
		val userId: Long = given()
			.pathParam("firstName", "Norbert")
			.pathParam("lastName", "Siegmund")
			.`when`()
			.post("/api/user/{lastName}/{firstName}")
			.then()
			.statusCode(Matchers.`is`(HttpStatus.SC_CREATED))
			.extract()
			.body().`as`(Long::class.java)
		val responseUser: User = given()
			.pathParam("id", userId)
			.`when`()
			.get("/api/user/{id}")
			.then()
			.statusCode(HttpStatus.SC_OK)
			.assertThat()
			.extract().`as`(User::class.java)

		// Did Norbert came back?
		assertThat(responseUser.firstName, Matchers.`is`("Norbert"))
		assertThat(responseUser.lastName, Matchers.`is`("Siegmund"))
	}

	@Test
	fun user_api_should_give_http_404_not_found_when_user_not_present_in_db() {
		val someId = 200L
		given()
			.pathParam("id", someId)
			.`when`()
			.get("/api/user/{id}")
			.then()
			.statusCode(HttpStatus.SC_NOT_FOUND)
	}

	@Test
	fun secured_api_should_react_with_unauthorized_per_default() {
		given()
			.`when`()
			.get("/api/secured")
			.then()
			.statusCode(HttpStatus.SC_UNAUTHORIZED)
	}

	@Test
	fun secured_api_should_give_http_200_when_authorized() {
		given()
			.auth().basic("admin", "nimda")
			.`when`()
			.get("/api/secured")
			.then()
			.statusCode(HttpStatus.SC_OK)
			.assertThat()
			.body(Matchers.`is`(Matchers.equalTo(BackendController.SECURED_TEXT)))
	}
}