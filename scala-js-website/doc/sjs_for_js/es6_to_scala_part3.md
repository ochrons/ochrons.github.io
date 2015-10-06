---
layout: doc
title: "From ES6 to Scala: Advanced"
---

Scala is a feature rich language that is easy to learn but takes time to master. Depending on your programming
background, typically you start by writing Scala as you would've written the language you know best (JavaScript, Java or
C# for example) and gradually learn more and more idiomatic Scala paradigms to use. In this section we cover some of the
more useful design patterns and features, to get you started quickly.

## Pattern matching

In the Basics part we already saw simple examples of _pattern matching_ as a replacement for JavaScript switch
statement. However, it can be used for much more, for example checking the type of input.

{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
function printType(o) {
  if (typeOf o === "string")
    console.log(`It's a string: ${o}`)
  else if (typeOf o === "number")
    console.log(`It's a number: ${o}`)
  else if (typeOf o === "boolean")
    console.log(`It's a boolean: ${o}`)
  else
    console.log(`It's something else`)
}
{% endhighlight %}
{% endcolumn %}
        
{% column 6 Scala %}
{% highlight scala %}
def printType(o: Any) {
  o match {
    case s: String =>
      println(s"It's a string: $s")
    case i: Int =>
      println(s"It's an int: $i")
    case b: Boolean =>
      println(s"It's a boolean: $b")
    case _ =>
      println("It's something else")
}
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

Pattern matching uses something called _partial functions_ which means it can be used in place of regular functions, for
example in a call to `filter` or `map`. You can also add a _guard clause_ in the form of an `if`, to limit the match. If
you need to match to a variable, use backticks to indicate that.

{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
function parse(str, magicKey) {
  let res = [];
  for(let c of str) {
    if (c === magicKey)
      res.push("magic");
    else if (c.match(/\d/))
      res.push("digit");
    else if (c.match(/\w/))
      res.push("letter");
    else if (c.match(/\s/))
      res.push(" ");
    else 
      res.push("char");
  }
  return res;
}
const r = parse("JB/007", '/');
// [letter, letter, magic, digit, digit, digit]
{% endhighlight %}
{% endcolumn %}
        
{% column 6 Scala %}
{% highlight scala %}
def parse(str: String, magicKey: Char)= {
  str.map {
    case `magicKey` =>
      "magic"
    case c if c.isDigit =>
      "digit"
    case c if c.isLetter =>
      "letter"
    case c if c.isWhitespace =>
      " "
    case c =>
      "char"
  }
}
val r = parse("JB/007", '/')
// Seq(letter, letter, magic, digit, digit, digit)
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

#### Destructuring

Where pattern matching really shines is at destructuring. This means matching to a more complex pattern and extracting
values inside that structure. ES6 also supports destructuring (yay!) in assignments and function parameters, but not
in matching.


{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
const person = {first: "James", last: "Bond", 
  age: 42};
const {first, last, age: years} = person;
// first = "James", last = "Bond", years = 42
const seq = [1, 2, 3, 4, 5];
const [a, b, ...c] = seq;
// a = 1, b = 2, c = [3, 4, 5]
const [,,,,x] = seq; // x = 5

const seq2 = [a, b].concat(c); // [1, 2, 3, 4, 5]
{% endhighlight %}
{% endcolumn %}
        
{% column 6 Scala %}
{% highlight scala %}
case class Person(first: String, last: String, 
  age: Int)
val person = Person("James", "Bond", 42)
val Person(first, last, years) = person
// first = "James", last = "Bond", years = 42
val seq = Seq(1, 2, 3, 4, 5)
val a +: b +: c = seq 
// a = 1, b = 2, c = Seq(3, 4, 5)
val _ :+ x = seq // x = 5

val seq2 = a +: b +: c // Seq(1, 2, 3, 4, 5)
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

In Scala the destructuring and rebuilding have nice symmetry making it easy to remember how to do it. ES6 also has quite
good symmetry, but there are some edge cases you need to take care of. The _rest_ parameter (`...c`) in ES6 can only be
applied at the last position, while in Scala you can also use it in the beginning by swapping the `+:` operator into
`:+`.

In pattern matching the use of destructuring results in clean, simple and understandable code.

{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
function ageSum(persons, family) {
  return persons.filter(p => p.last === family)
    .reduce((a, p) => a + p.age, 0);
}
const persons = [
  {first: "James", last: "Bond", age: 42},
  {first: "Hillary", last: "Bond", age: 35},
  {first: "James", last: "Smith", age: 55}
];

ageSum(persons, "Bond") == 77;
{% endhighlight %}
{% endcolumn %}
        
{% column 6 Scala %}
{% highlight scala %}
def ageSum(persons: Seq[Person], 
           family: String): Int = {
  persons.collect {
    case Person(_, `family`, age) => age
  }.sum
}
val persons = Seq(
  Person("James", "Bond", 42),
  Person("Hillary", "Bond", 35),
  Person("James", "Smith", 55)
)

ageSum(persons, "Bond") == 77
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

We could've implemented the Scala function using a `filter` and `foldLeft`, but it is more understandable using
`collect` and pattern matching. It would be read as "Collect every person with a last name equaling `family` and extract
the age of those persons. Then sum up the ages."

Another good use case for pattern matching is regular expressions (also in ES6!). Let's extract a date in different
formats.

{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
function getDate(d) {
  const YMD = /(\d{4})-(\d{1,2})-(\d{1,2})/
  const MDY = /(\d{1,2})\/(\d{1,2})\/(\d{4})/
  const DMY = /(\d{1,2})\.(\d{1,2})\.(\d{4})/
  {
    const [, year, month, day] = YMD.exec(d) || [];
    if (year !== undefined)
      return {year: year, month: month, day: day};
    else {
      const [, month, day, year] = 
        MDY.exec(d) || [];
      if (year !== undefined)
        return {year: year, month: month, day: day};
      else {
        const [, day, month, year] = 
          DMY.exec(d) || [];
        if (year !== undefined)
          return {year: year, month: month, 
            day: day};
      }
    }
  }
  throw "Invalid date!";
}
getDate("2015-10-9"); // {year:2015,month:10,day:9}
getDate("10/9/2015"); // {year:2015,month:10,day:9}
getDate("9.10.2015"); // {year:2015,month:10,day:9}
getDate("10 Nov 2015") // exception
{% endhighlight %}
{% endcolumn %}
        
{% column 6 Scala %}
{% highlight scala %}
case class Date(year: Int, month: Int, day: Int)

def getDate(d: String): Date = {
  val YMD = """(\d{4})-(\d{1,2})-(\d{1,2})""".r
  val MDY = """(\d{1,2})/(\d{1,2})/(\d{4})""".r
  val DMY = """(\d{1,2})\.(\d{1,2})\.(\d{4})""".r
  d match {
    case YMD(year, month, day) => 
      Date(year.toInt, month.toInt, day.toInt)
    case MDY(month, day, year) =>
      Date(year.toInt, month.toInt, day.toInt)
    case DMY(day, month, year) =>
      Date(year.toInt, month.toInt, day.toInt)
    case _ => 
      throw new Exception("Invalid date!")
  }
}

getDate("2015-10-9") // = Date(2015,10,9)
getDate("10/9/2015") // = Date(2015,10,9)
getDate("9.10.2015") // = Date(2015,10,9)
getDate("10 Nov 2015") // exception
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

Here we use triple-quoted strings that allow us to write regex without escaping special characters. The string is
converted into a `Regex` object with the `.r` method. Because regex extracts strings, we need to convert matched groups
to integers ourselves.

## Implicits

... placeholder ...

#### Implicit conversions and duck typing

... placeholder ...


## Recursion




{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
{% endhighlight %}
{% endcolumn %}
        
{% column 6 Scala %}
{% highlight scala %}
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}
