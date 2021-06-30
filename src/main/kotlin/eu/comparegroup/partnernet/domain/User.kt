@file:JvmName("User")
@file:JvmMultifileClass
package kotlin.eu.comparegroup.partnernet.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
data class User(
	val firstName: String? = null,
	val lastName: String? = null,
) {
	// PrimaryKey
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var id: Long = 0
	override fun toString(): String =
			"User[id=$id, firstName=$firstName, lastName=$lastName]"
}