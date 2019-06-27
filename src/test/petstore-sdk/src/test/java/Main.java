import io.github.erfangc.sdk.apis.CompanySdk;
import io.github.erfangc.sdk.operations.PetStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        final PetStore petStore = CompanySdk.petStore();
        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(petStore.listPets(1)));
    }
}
