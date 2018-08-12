package server

object FestServerStarter{

  @JvmStatic
  fun main(args: Array<String>) {
    with(FestServer()) {
      start()
      performOnApp { System.out.println("Hello from Server!") }
    }

  }

}