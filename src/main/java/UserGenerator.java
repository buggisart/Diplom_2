import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {

        public static User getRandomWithAllParams() {
            final String email = RandomStringUtils.randomAlphabetic(10) + "@gmail.com";
            final String password = RandomStringUtils.randomAlphabetic(10);
            final String name = RandomStringUtils.randomAlphabetic(10);
            return new User(email, password, name);
        }
        public static User getRandomWithoutPassword(){
            final String email = RandomStringUtils.randomAlphabetic(10) + "@gmail.com";
            final String name = RandomStringUtils.randomAlphabetic(10);
            return new User(email, null, name);
        }

        public static User getRandomWithoutLogin(){
            final String password = RandomStringUtils.randomAlphabetic(10);
            final String name = RandomStringUtils.randomAlphabetic(10);
            return new User(null, password, name);
        }

        public static User getRandomWithEmailAndName() {
            final String email = RandomStringUtils.randomAlphabetic(10) + "@gmail.com";
            final String name = RandomStringUtils.randomAlphabetic(10);
            return new User(email, name);
        }
    }

