data class CreditRequest(
    val creditValue: Double,
    val dayFirstOfInstallment: Int,
    val numberOfInstallments: Int,
    val customerId: Long
)

data class Credit(
    val creditCode: String,
    val creditValue: Double,
    val numberOfInstallment: Int,
    val status: String,
    val emailCustomer: String,
    val incomeCustomer: Double
)

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/credits")
class CreditController {

    private val MAX_INSTALLMENTS = 48
    private val MAX_DAYS_FIRST_INSTALLMENT = 90

    private val creditRequests = mutableListOf<Credit>()

    @PostMapping("/request")
    fun requestCredit(@RequestBody creditRequest: CreditRequest): ResponseEntity<String> {
        // Verificações de validação
        if (creditRequest.numberOfInstallments > MAX_INSTALLMENTS) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Número de parcelas excede o máximo permitido")
        }
        val today = LocalDate.now()
        val requestedDate = today.plusDays(creditRequest.dayFirstOfInstallment.toLong())
        if (today.plusDays(MAX_DAYS_FIRST_INSTALLMENT.toLong()) < requestedDate) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data da primeira parcela deve ser dentro dos próximos 3 meses")
        }

        // Geração de código de crédito (pode ser melhorado para ser único)
        val creditCode = "CR-${System.currentTimeMillis()}"

        // Criar objeto de crédito e adicionar à lista (simulando armazenamento)
        val newCredit = Credit(
            creditCode = creditCode,
            creditValue = creditRequest.creditValue,
            numberOfInstallment = creditRequest.numberOfInstallments,
            status = "Pendente",
            emailCustomer = "customer@example.com", // Simulado, pode ser obtido de acordo com o customerId
            incomeCustomer = 5000.0 // Simulado, pode ser obtido de acordo com o customerId
        )
        creditRequests.add(newCredit)

        return ResponseEntity.status(HttpStatus.CREATED).body("Crédito solicitado com sucesso. Código: $creditCode")
    }

    @GetMapping("/list/{customerId}")
    fun listCredits(@PathVariable customerId: Long): List<Credit> {
        return creditRequests.filter { it.emailCustomer == "customer@example.com" } // Simulado, filtrando pelo cliente
    }

    @GetMapping("/view/{customerId}/{creditCode}")
    fun viewCredit(@PathVariable customerId: Long, @PathVariable creditCode: String): ResponseEntity<Credit> {
        val credit = creditRequests.find { it.creditCode == creditCode && it.emailCustomer == "customer@example.com" } // Simulado, filtrando pelo cliente
        return if (credit != null) {
            ResponseEntity.ok(credit)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
