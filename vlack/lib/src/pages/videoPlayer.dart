import 'package:flutter/material.dart';
import 'package:video_player/video_player.dart';
import 'package:chewie/chewie.dart';

class VideoPlayerWidget extends StatefulWidget{
  final String videoUrl;

  const VideoPlayerWidget({super.key, required this.videoUrl});

  @override
  _VideoPlayerWidgetState createState() => _VideoPlayerWidgetState();
}

class _VideoPlayerWidgetState extends State<VideoPlayerWidget>{
  late VideoPlayerController _videoController;
  late ChewieController _chewieController;

  @override
  void initState(){
    super.initState();
    _videoController = VideoPlayerController.networkUrl(Uri.parse(widget.videoUrl));

    _chewieController = ChewieController(
      videoPlayerController: _videoController,
      aspectRatio: 16/9,
      autoInitialize: true,
      autoPlay: false,
      looping: false,
      );
    
  }
  
  @override
  Widget build(BuildContext context) {
      return SizedBox(
        height: MediaQuery.of(context).orientation == Orientation.landscape
        ? null
        : 200,
        child: Chewie(controller: _chewieController),
      );
  }

  @override
  void dispose(){
    super.dispose();
    _videoController.dispose();
    _chewieController.dispose();
  }
}

