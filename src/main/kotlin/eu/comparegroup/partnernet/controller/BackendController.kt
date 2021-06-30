package kotlin.eu.comparegroup.partnernet.controller


import kotlin.eu.comparegroup.partnernet.domain.User
import kotlin.eu.comparegroup.partnernet.exception.UserNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.eu.comparegroup.partnernet.repository.UserRepository

@RestController // @RestController instead of vanilla @Controller and @ResponseBody
@RequestMapping("/api")
class BackendController @Autowired constructor(private val userRepository: UserRepository) {

	//	@ResponseBody
	@RequestMapping(path = ["/hello"])
	fun sayHello(): String {
		LOG.info("GET called on /hello resource")
		return HELLO_TEXT
	}

	//	@ResponseBody
	@RequestMapping(path = ["/user/{lastName}/{firstName}"], method = [RequestMethod.POST])
	@ResponseStatus(HttpStatus.CREATED)
	fun addNewUser(
		@PathVariable("lastName") lastName: String?,
		@PathVariable("firstName") firstName: String?
	): Long {
		val savedUser = userRepository.save(User(firstName, lastName))
		LOG.info("$savedUser successfully saved into DB")
		return savedUser.id
	}

	//	@ResponseBody
	@GetMapping(path = ["/user/{id}"])
	fun getUserById(@PathVariable("id") id: Long): User {
		return userRepository.findById(id).map { user: User? ->
			LOG.info(
				"Reading user with id $id from database."
			)
			user
		}.orElseThrow {
			UserNotFoundException(
				"The user with the id $id couldn't be found in the database."
			)
		}!!
	}

	//	@ResponseBody
	@get:RequestMapping(path = ["/secured"], method = [RequestMethod.GET])
	val secured: String
		get() {
			LOG.info("GET successfully called on /secured resource")
			return SECURED_TEXT
		}

	companion object {

		private val LOG = LoggerFactory.getLogger(BackendController::class.java)
		const val HELLO_TEXT = "Hello from Spring Boot Backend!"
		const val SECURED_TEXT = "Hello from the secured resource!"
	}
}