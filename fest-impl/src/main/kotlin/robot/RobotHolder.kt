package robot

import org.fest.swing.core.BasicRobot
import org.fest.swing.core.Robot

object RobotHolder {

    private val myRobot: Robot by lazy {
        BasicRobot.robotWithCurrentAwtHierarchy()
    }

    @JvmStatic
    fun getRobot(): Robot {
        return myRobot
    }
}