import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

data class Customer(
    var id: Long = 0,
    var firstName: String = "",
    var lastName: String = "",
    var cpf: String = "",
    var income: Double = 0.0,
    var email: String = "",
    var password: String = "",
    var zipCode: String = "",
    var street: String = ""
)

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@RestController
@RequestMapping("/customers")
class CustomerController {
    private val customers = mutableListOf<Customer>()
    private var customerIdCounter = 1L
    
    @PostMapping("/register")
    fun registerCustomer(@RequestBody customer: Customer): ResponseEntity<String> {
        customers.add(customer.apply { id = customerIdCounter++ })
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer registered successfully")
    }
    
    @PostMapping("/edit")
    fun editCustomer(@RequestBody customer: Customer): ResponseEntity<Customer> {
        val existingCustomer = customers.find { it.id == customer.id }
        existingCustomer?.apply {
            firstName = customer.firstName
            lastName = customer.lastName
            income = customer.income
            zipCode = customer.zipCode
            street = customer.street
        }
        return ResponseEntity.ok(existingCustomer)
    }
    
    @GetMapping("/profile/{id}")
    fun viewProfile(@PathVariable id: Long): ResponseEntity<Customer> {
        val customer = customers.find { it.id == id }
        return if (customer != null) {
            ResponseEntity.ok(customer)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @DeleteMapping("/delete/{id}")
    fun deleteCustomer(@PathVariable id: Long): ResponseEntity<Unit> {
        val removed = customers.removeIf { it.id == id }
        return if (removed) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
