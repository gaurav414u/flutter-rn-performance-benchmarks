import React, { useEffect, useState, useRef } from 'react';

import {
  SafeAreaView,
  StyleSheet,
  View,
  FlatList,
  StatusBar,
  Image,
  Dimensions,
} from 'react-native';
import FastImage from 'react-native-fast-image';
import Animated, {
  useSharedValue,
  useAnimatedStyle,
  withTiming,
  withRepeat,
  Easing,
  interpolate,
} from 'react-native-reanimated';
import { NativeModules } from 'react-native';

const { ReportFullyDrawn } = NativeModules;


const AnimatedFastImage = Animated.createAnimatedComponent(FastImage);

const IMAGES = {
  image0: require('./images/0.jpeg'),
  image1: require('./images/1.jpeg'),
  image2: require('./images/2.jpeg'),
  image3: require('./images/3.jpeg'),
  image4: require('./images/4.jpeg'),
  image5: require('./images/5.jpeg'),
  image6: require('./images/6.jpeg'),
  image7: require('./images/7.jpeg'),
  image8: require('./images/8.jpeg'),
  image9: require('./images/9.jpeg'),
  image10: require('./images/10.jpeg'),
  image11: require('./images/11.jpeg'),
  image12: require('./images/12.jpeg'),
  image13: require('./images/13.jpeg'),
  image14: require('./images/14.jpeg'),
  image15: require('./images/15.jpeg'),
  image16: require('./images/16.jpeg'),
  image17: require('./images/17.jpeg'),
  image18: require('./images/18.jpeg'),
  image19: require('./images/19.jpeg'),
};

function getImage(num) {
  return IMAGES['image' + (num % 20)];
}

let data = Array(200);
for (let i = 0; i < data.length; i++) {
  data[i] = {
    key: String(i),
    src: getImage(i),
  };
}


const App = () => {
  const globalAnimation = useSharedValue(0);
  const [loadedImages, setLoadedImages] = useState(0);
  const hasReported = useRef(false);

  useEffect(() => {
    globalAnimation.value = withRepeat(
      withTiming(1, {
        duration: 5000,
        easing: Easing.linear,
      }),
      -1
    );
  }, [globalAnimation]);

  useEffect(() => {
    if (loadedImages === data.length && !hasReported.current) {
      hasReported.current = true;
      ReportFullyDrawn.reportFullyDrawn();
    }
  }, [loadedImages]);

  const handleImageLoad = () => {
    setLoadedImages((prev) => prev + 1);
  };

  return (
    <>
      <StatusBar barStyle="dark-content" />
      <SafeAreaView>
        <FlatList
          data={data}
          renderItem={({ item }) => {
            let index = item.key % 3;
            if (index === 0) {
              return <RotatingImage index={item.key} onLoad={handleImageLoad} />;
            }
            if (index === 1) {
              return <ScalingImage index={item.key} onLoad={handleImageLoad} />;
            }
            return <FadingImage source={item.src} onLoad={handleImageLoad} />;
          }}
          numColumns={10}
          keyExtractor={(item, index) => index.toString()}
        />
      </SafeAreaView>
    </>
  );
};

const RotatingImage = ({ index, onLoad }) => {
  const rotateValue = useSharedValue(0);

  useEffect(() => {
    rotateValue.value = withRepeat(
      withTiming(360, {
        duration: 5000,
        easing: Easing.linear,
      }),
      -1
    );
  }, [rotateValue]);

  const animatedStyle = useAnimatedStyle(() => ({
    transform: [{ rotate: `${rotateValue.value}deg` }],
  }));

  return (
    <AnimatedFastImage
      style={[styles.imageThumbnail, animatedStyle]}
      source={getImage(index)}
      onLoad={onLoad}
      resizeMode={'stretch'}
    />
  );
};

const ScalingImage = ({ index, onLoad }) => {
  const scaleValue = useSharedValue(0);

  useEffect(() => {
    scaleValue.value = withRepeat(
      withTiming(1, {
        duration: 5000,
        easing: Easing.linear,
      }),
      -1
    );
  }, [scaleValue]);

  const animatedStyle = useAnimatedStyle(() => ({
    transform: [{ scale: scaleValue.value }],
  }));

  return (
    <AnimatedFastImage
      style={[styles.imageThumbnail, animatedStyle]}
      source={getImage(index)}
      onLoad={onLoad}
      resizeMode={'stretch'}
    />
  );
};

const FadingImage = ({ source, onLoad }) => {
  const opacityValue = useSharedValue(0);

  useEffect(() => {
    opacityValue.value = withRepeat(
      withTiming(1, {
        duration: 5000,
        easing: Easing.linear,
      }),
      -1
    );
  }, [opacityValue]);

  const animatedStyle = useAnimatedStyle(() => ({
    opacity: opacityValue.value,
  }));

  return (
    <View style={styles.centeredContainer}>
      <AnimatedFastImage
        style={[styles.imageThumbnail, animatedStyle]}
        source={source}
        onLoad={onLoad}
        resizeMode={'stretch'}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  imageThumbnail: {
    justifyContent: 'center',
    alignItems: 'center',
    width: Dimensions.get('window').width / 10,
    height: Dimensions.get('window').width / 10,
  },
  centeredContainer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});

export default App;