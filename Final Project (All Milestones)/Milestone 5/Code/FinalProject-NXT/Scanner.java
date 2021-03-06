����   3 t  robot/Scanner  java/lang/Object lightSensor Llejos/nxt/LightSensor; ultraSensor Llejos/nxt/UltrasonicSensor; motor Llejos/nxt/NXTRegulatedMotor; beaconBearings [I _max I _THREADHOLD <init> S(Llejos/nxt/NXTRegulatedMotor;Llejos/nxt/LightSensor;Llejos/nxt/UltrasonicSensor;)V Code
     ()V	    	    	   	 
	    	    
 ! # " lejos/nxt/LightSensor $ % setFloodlight (Z)V	  '   LineNumberTable LocalVariableTable this Lrobot/Scanner; mtr lsen usen 	lightScan (II)V
 2 4 3 lejos/nxt/NXTRegulatedMotor 5 6 rotateTo (I)V
 2 8 5 9 (IZ)V
 2 ; < = getTachoCount ()I
 ! ? @ = getLightValue	 B D C lejos/nxt/Sound E  PIANO
 B G H I playNote ([III)V
 2 K L M isMoving ()Z
  O P Q calculateBearings ([I[I)V 
startAngle endAngle counterClockwiseBearings clockwiseBearings startAngles 	endAngles highestLightValue counterClockwise 	clockwise isCounterClockwise Z isAssignedToCCW isAssignedToCW i newAngle currentLightValue StackMapTable  getEchoDistance (F)I
 g i h lejos/nxt/UltrasonicSensor j = getDistance angle F rotateHeadTo (F)V getHeadAngle getBearings ()[I 
SourceFile Scanner.java !                 	 
                          �     0*� *� *#� *+� *,� *-� *� �  *�
� &�    (   & 	                 (  /  )   *    0 * +     0 , 
    0 -     0 .    / 0    �    :*� � 1�
Y*� OY*� ON�
Y*� OY*� O:�
YOYO:�
YOYO:666	6
666� �..� � 6
*� .� 7� �*� � :6*� � >6*� � .� '6
� -O6
� c	O6� V*� � %
�  � � �6� A �� F� +*� � "
� � 	� �	6� A�� F*� � J��`6���0*-� N�    (   � "   &  (  ) - + : , G . P / Y 2 _ 3 s 5 � 7 � 8 � : � < � = � ? � @ � A � D � E � F � I � J � K � L � M N O P 7& U) 22 Y9 Z )   �   : * +    : R    : S     T   - U   :  V   G � W   J � X   M � Y   P � Z  	 S � [ \ 
 V � ] \  Y � ^ \  \ � _   � � `   � � a   b   0 	� _   c c c c  @� 3*� '  P Q     �     D>� ;+.*� � +,d.O,.*� � ,+d.O*� &+.,d.`lO�+���ű    (   "    d  e  f  h ! i ) l : d C n )   *    D * +     D T     D U    A _   b   	 �   d e     I     *� #�� 1*� � f�    (   
    v 	 w )        * +      k l   d =     2     *� � f�    (        )        * +    m n     B     
*� #�� 1�    (   
    � 	 � )       
 * +     
 k l   o =     2     *� � :�    (       � )        * +    p q     /     *� &�    (       � )        * +    r    s