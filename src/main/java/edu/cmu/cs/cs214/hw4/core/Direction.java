package edu.cmu.cs.cs214.hw4.core;

public enum Direction {
    LEFT {
        /**
         * the reverse direction of left which is right
         * @return the reverse direction
         */
        public Direction reverse() {
            return RIGHT;
        }
    },
    RIGHT {
        /**
         * the reverse direction of right which is left
         * @return the reverse direction
         */
        public Direction reverse() {
            return LEFT;
        }
    },
    UP {
        /**
         * the reverse direction of up which is down
         * @return the reverse direction
         */
        public Direction reverse() {
            return DOWN;
        }
    },
    DOWN {
        /**
         * the reverse direction of down which is up
         * @return the reverse direction
         */
        public Direction reverse() {
            return UP;
        }
    },
    CENTER {
    };

    /**
     * gets the reverse direction of this direction
     * @return reverse direction
     */
    public Direction reverse(){
        return this.reverse();
    };
}
