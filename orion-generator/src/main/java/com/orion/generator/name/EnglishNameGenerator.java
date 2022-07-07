package com.orion.generator.name;

import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.random.Randoms;

/**
 * 英文名称生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/11 12:38
 */
public class EnglishNameGenerator {

    private static final String[] NAMES;

    private EnglishNameGenerator() {
    }

    static {
        NAMES = new String[]{
                "Aaron", "Abel", "Abraham", "Adam", "Adrian",
                "Aidan", "Alva", "Alex", "Alexander", "Alan",
                "Albert", "Alfred", "Andrew", "Andy", "Angus",
                "Anthony", "Apollo", "Arnold", "Arthur", "August",
                "Austin", "Ben", "Benjamin", "Bert", "Benson",
                "Bill", "Billy", "Blake", "Bob", "Bobby", "Brad",
                "Brandon", "Brant", "Brent", "Brian", "Brown",
                "Bruce", "Caleb", "Cameron", "Carl", "Carlos",
                "Cary", "Caspar", "Cecil", "Charles", "Cheney",
                "Chris", "Christian", "Christopher", "Clark",
                "Cliff", "Cody", "Cole", "Colin", "Cosmo", "Daniel",
                "Denny", "Darwin", "David", "Dennis", "Derek",
                "Dick", "Donald", "Douglas", "Duke", "Dylan",
                "Eddie", "Edgar", "Edison", "Edmund", "Edward",
                "Edwin", "Elijah", "Elliott", "Elvis", "Eric",
                "Ethan", "Eugene", "Evan", "Enterprise", "Ford",
                "Francis", "Frank", "Franklin", "Fred", "Gabriel",
                "Gaby", "Garfield", "Gary", "Gavin", "Geoffrey",
                "George", "Gino", "Glen", "Glendon", "Hank", "Hardy",
                "Harrison", "Harry", "Hayden", "Henry", "Hilton", "Hugo",
                "Hunk", "Howard", "Henry", "Ian", "Ignativs", "Ivan",
                "Isaac", "Isaiah", "Jack", "Jackson", "Jacob", "James",
                "Jason", "Jay", "Jeffery", "Jerome", "Jerry", "Jesse",
                "Jim", "Jimmy", "Joe", "John", "Johnny", "Jonathan",
                "Jordan", "Jose", "Joshua", "Justin", "Keith", "Ken",
                "Kennedy", "Kenneth", "Kenny", "Kevin", "Kyle", "Lance",
                "Larry", "Laurent", "Lawrence", "Leander", "Lee", "Leo",
                "Leonard", "Leopold", "Leslie", "Loren", "Lori", "Lorin",
                "Louis", "Luke", "Marcus", "Marcy", "Mark", "Marks",
                "Mars", "Marshal", "Martin", "Marvin", "Mason", "Matthew",
                "Max", "Michael", "Mickey", "Mike", "Nathan", "Nathaniel",
                "Neil", "Nelson", "Nicholas", "Nick", "Noah", "Norman",
                "Oliver", "Oscar", "Owen", "Patrick", "Paul", "Peter",
                "Philip", "Phoebe", "Quentin", "Randall", "Randolph", "Randy",
                "Ray", "Raymond", "Reed", "Rex", "Richard", "Richie", "Riley",
                "Robert", "Robin", "Robinson", "Rock", "Roger", "Ronald",
                "Rowan", "Roy", "Ryan", "Sam", "Sammy", "Samuel", "Scott",
                "Sean", "Shawn", "Sidney", "Simon", "Solomon", "Spark",
                "Spencer", "Spike", "Stanley", "Steve", "Steven", "Stewart",
                "Stuart", "Terence", "Terry", "Ted", "Thomas", "Tim",
                "Timothy", "Todd", "Tommy", "Tom", "Thomas", "Tony",
                "Tyler", "Ulysses", "Van", "Vern", "Vernon", "Victor",
                "Vincent", "Warner", "Warren", "Wayne", "Wesley", "William",
                "Willy", "Zack", "Zachary", "Abigail", "Abby", "Ada",
                "Adelaide", "Adeline", "Alexandra", "Ailsa", "Aimee", "Alexis",
                "Alice", "Alicia", "Alina", "Allison", "Alyssa", "Amanda",
                "Amy", "Amber", "Anastasia", "Andrea", "Angel", "Angela",
                "Angelia", "Angelina", "Ann", "Anna", "Anne", "Annie", "Anita",
                "Ariel", "April", "Ashley", "Audrey", "Aviva", "Barbara", "Barbie",
                "Beata", "Beatrice", "Becky", "Bella", "Bess", "Bette", "Betty",
                "Blanche", "Bonnie", "Brenda", "Brianna", "Britney", "Brittany",
                "Camille", "Candice", "Candy", "Carina", "Carmen", "Carol",
                "Caroline", "Carry", "Carrie", "Cassandra", "Cassie", "Catherine",
                "Cathy", "Chelsea", "Charlene", "Charlotte", "Cherry", "Claire",
                "Cheryl", "Chloe", "Chris", "Christina", "Christine", "Christy",
                "Claudia", "Clement", "Cloris", "Connie", "Constance", "Cora",
                "Corrine", "Crystal", "Daisy", "Daphne", "Darcy", "Dave",
                "Debbie", "Deborah", "Debra", "Demi", "Diana", "Dolores",
                "Donna", "Dora", "Doris", "Edith", "Editha", "Elaine",
                "Eleanor", "Elizabeth", "Ella", "Ellen", "Ellie", "Frederica",
                "Emerald", "Emily", "Emma", "Enid", "Elsa", "Cindy",
                "Erica", "Estelle", "Esther", "Eudora", "Eva", "Eve", "Evelyn",
                "Fannie", "Fay", "Fiona", "Flora", "Florence", "Frances",
                "Frieda", "Flta", "Gina", "Gillian", "Gladys", "Haley", "Hebe",
                "Ishara", "Gloria", "Grace", "Grace", "Greta", "Gwendolyn", "Hannah",
                "Helena", "Hellen", "Henna", "Heidi", "Hillary", "Ingrid",
                "Isabella", "Irene", "Iris", "Ivy", "Kathy", "Katie", "Kitty",
                "Jacqueline", "Jade", "Jamie", "Jane", "Janet", "Jasmine",
                "Jean", "Jenna", "Jennifer", "Jenny", "Jessica", "Jessie",
                "Joan", "Joanna", "Jocelyn", "Joliet", "Josephine", "Josie",
                "Joy", "Jill", "Joyce", "Judith", "Judy", "Julia", "Juliana",
                "Julie", "June", "Karen", "Karida", "Katherine", "Kate",
                "Katrina", "Kay", "Kayla", "Kelly", "Kelsey", "Kimberly",
                "Lassie", "Laura", "Lauren", "Lena", "Lydia", "Lillian",
                "Lily", "Linda", "lindsay", "Lisa", "Liz", "Lareina",
                "Lora", "Lorraine", "Louisa", "Louise", "Lucia", "Lucy",
                "Lucine", "Lulu", "Lydia", "Lynn", "Mabel", "Maureen",
                "Madeline", "Maggie", "Mamie", "Manda", "Mandy", "Margaret",
                "Mariah", "Marilyn", "Martha", "Mavis", "Mary", "Matilda",
                "Mavis", "Maxine", "May", "Mayme", "Megan", "Milly", "Michelle",
                "Melinda", "Melissa", "Melody", "Mercedes", "Meredith", "Mia",
                "Miranda", "Miriam", "Miya", "Molly", "Monica", "Morgan", "Nancy",
                "Natalie", "Natasha", "Nina", "Nora", "Norma", "Nydia", "Octavia",
                "Olina", "Olivia", "Ophelia", "Oprah", "Pamela", "Patricia",
                "Nicole", "Nikita", "Patty", "Paula", "Pauline", "Pearl", "Sami",
                "Peggy", "Philomena", "Phoebe", "Phyllis", "Polly", "Priscilla",
                "Quentina", "Rachel", "Rebecca", "Regina", "Rita", "Samantha",
                "Rose", "Roxanne", "Ruth", "Sabrina", "Sally", "Sandra",
                "Sandra", "Sandy", "Sarah", "Savannah", "Scarlett", "Selma",
                "Selina", "Serena", "Sharon", "Sheila", "Shelley", "Sherry",
                "Shirley", "Sierra", "Silvia", "Sonia", "Sophia",
                "Stacy", "Stark", "Stella", "Stephanie", "Sue", "Sunny",
                "Susan", "Tamara", "Tammy", "Tanya", "Tasha", "Teresa",
                "Tess", "Tiffany", "Tina", "Tonya", "Tracy",
                "Ursula", "Vanessa", "Venus", "Vera", "Vicky",
                "Victoria", "Violet", "Virginia", "Vita", "Vivian"
        };
    }

    /**
     * 生成姓名 1个单词
     *
     * @return 姓名
     */
    public static String generatorName1() {
        return Arrays1.random(NAMES);
    }

    /**
     * 生成姓名 2个单词
     *
     * @return 姓名
     */
    public static String generatorName2() {
        return Arrays1.random(NAMES) + Strings.SPACE + Arrays1.random(NAMES);
    }

    /**
     * 生成姓名 1/2 个单词
     *
     * @return 姓名
     */
    public static String generatorName() {
        if (Randoms.randomBoolean(3)) {
            return generatorName2();
        } else {
            return generatorName1();
        }
    }

}
