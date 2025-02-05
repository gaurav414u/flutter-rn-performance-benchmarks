import React, {useRef, useEffect, useState, useCallback} from 'react';
import {StyleSheet, Text, View, Button} from 'react-native';
import FastImage from 'react-native-fast-image';
import Animated, {
  Easing,
  useSharedValue,
  useAnimatedStyle,
  withTiming,
  withRepeat,
} from 'react-native-reanimated';
import {FlashList} from '@shopify/flash-list';

const AnimatedFastImage = Animated.createAnimatedComponent(FastImage);

const data = Array(1001)
  .fill(null)
  .map((_, i) => ({
    key: String(i),
    color: `rgb(${Math.floor(Math.random() * 256)},${Math.floor(
      Math.random() * 256,
    )},${Math.floor(Math.random() * 256)})`,
  }));

const intervalTime = 300;
const scrollDuration = 500000;

const App = () => {
  const flatListRef = useRef(null);
  const [currentOffset, setCurrentOffset] = useState(0);
  const timerRef = useRef(null);

  const scrollOffset = useCallback((speed, totalScroll) => {
    setCurrentOffset(prevOffset => {
      const newOffset = prevOffset + speed;
      if (newOffset >= totalScroll) {
        stopAutoPlay();
        return prevOffset;
      }
      flatListRef.current?.scrollToOffset({offset: newOffset, animated: true});
      return newOffset;
    });
  }, []);

  const startAutoPlay = useCallback(() => {
    const totalScrollDistance = data.length * (styles.item.height + 16);
    const steps = scrollDuration / intervalTime;
    const scrollSpeed = totalScrollDistance / steps;

    stopAutoPlay();
    timerRef.current = setInterval(
      () => scrollOffset(scrollSpeed, totalScrollDistance),
      intervalTime,
    );
  }, [scrollOffset]);

  const stopAutoPlay = useCallback(() => {
    if (timerRef.current) {
      clearInterval(timerRef.current);
      timerRef.current = null;
    }
  }, []);

  useEffect(() => {
    return () => stopAutoPlay();
  }, [stopAutoPlay]);

  return (
    <View style={styles.container}>
      <FlashList
        ref={flatListRef}
        testID={'long_list'}
        accessibilityLabel={'long_list'}
        data={data}
        ItemSeparatorComponent={FlatListItemSeparator}
        keyExtractor={item => item.key}
        renderItem={({item}) => (
          <CustomRow index={item.key} color={item.color} label={item.key} />
        )}
      />
      <View style={{position: 'absolute', top: 16, left: 16, right: 16}}>
        <Button onPress={startAutoPlay} title="Start scroll" />
      </View>
    </View>
  );
};

const FlatListItemSeparator = () => (
  <View style={{height: 16, width: '100%', backgroundColor: '#FFF'}} />
);
const IMAGES = [
  require('./assets/images/0.jpeg'),
  require('./assets/images/1.jpeg'),
  require('./assets/images/2.jpeg'),
  require('./assets/images/3.jpeg'),
  require('./assets/images/4.jpeg'),
  require('./assets/images/5.jpeg'),
  require('./assets/images/6.jpeg'),
  require('./assets/images/7.jpeg'),
  require('./assets/images/8.jpeg'),
  require('./assets/images/9.jpeg'),
  require('./assets/images/10.jpeg'),
  require('./assets/images/11.jpeg'),
  require('./assets/images/12.jpeg'),
  require('./assets/images/13.jpeg'),
  require('./assets/images/14.jpeg'),
  require('./assets/images/15.jpeg'),
  require('./assets/images/16.jpeg'),
  require('./assets/images/17.jpeg'),
  require('./assets/images/18.jpeg'),
  require('./assets/images/19.jpeg'),
];

const getImage = num => IMAGES[num % 20];

const CustomRow = ({index, color, label}) => {
  const spinValue = useSharedValue(0);

  const animatedStyle = useAnimatedStyle(() => ({
    transform: [{rotate: `${spinValue.value}deg`}],
  }));

  useEffect(() => {
    spinValue.value = withRepeat(
      withTiming(360, {duration: 5000, easing: Easing.linear}),
      -1,
    );
  }, [spinValue]);

  return (
    <View style={[styles.item_container, {backgroundColor: color}]}>
      <FastImage
        style={styles.image}
        source={getImage(index)}
        resizeMode={'stretch'}
      />
      <AnimatedFastImage
        style={[styles.image, animatedStyle]}
        source={getImage(index)}
        resizeMode={'stretch'}
        fadeDuration={0}
      />
      <Text accessibilityLabel={label} style={styles.item}>
        {index}
      </Text>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {flex: 1},
  item_container: {height: 100, flexDirection: 'row'},
  image: {height: 100, width: 100},
  item: {
    textAlign: 'center',
    textAlignVertical: 'center',
    justifyContent: 'center',
    fontSize: 18,
    height: 100,
  },
});

export default App;
