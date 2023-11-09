# -*- coding: utf-8 -*-
from flask import Flask, send_file, request  # 간단히 플라스크 서버를 만든다
import torch
import torchvision.transforms as transforms
from video_diffusion_pytorch import Unet3D, GaussianDiffusion
from PIL import Image
import io

app = Flask(__name__)


# @app.route('/', methods=['POST'])
def receive_from_spring():
    # 스프링 부트에서 전송한 데이터 받기
    received_data = request.data.decode("utf-8")

    return received_data


def model(text):

    model = Unet3D(
        dim=64,
        # this must be set to True to auto-use the bert model dimensions
        use_bert_text_cond=True,
        dim_mults=(1, 2, 4, 8),
    )

    diffusion = GaussianDiffusion(
        model,
        image_size=32,    # height and width of frames
        num_frames=5,     # number of video frames
        timesteps=1000,   # number of steps
        loss_type='l1',    # L1 or L2
    )

    # video (batch, channels, frames, height, width)
    videos = torch.randn(1, 3, 5, 32, 32)

    loss = diffusion(videos, cond=text)
    loss.backward()
    # after a lot of training

    sampled_videos = diffusion.sample(cond=text, cond_scale=2)

    return sampled_videos


def create_gif(gif_tensor):
    frames = []
    transform = transforms.ToPILImage()
    for f in range(5):
        pillow_image = transform(gif_tensor[:, f, :, :])
        frames.append(pillow_image)

    # 첫 번째 프레임을 기본으로 설정하여 GIF를 만듭니다.
    gif_bytes = io.BytesIO()
    frames[0].save(
        gif_bytes,
        save_all=True,
        append_images=frames[1:],
        format="GIF",
        loop=0,
        duration=200  # 각 프레임 간의 시간 (200ms)
    )
    gif_bytes.seek(0)
    return gif_bytes


@app.route("/", methods=['POST', 'GET'])
def index():

    prompt = receive_from_spring()
    print(prompt)

    text = [
        prompt,
    ]

    tensor_img = model(text)[0, :, :, :, :].squeeze()
    gif_bytes = create_gif(tensor_img)
    return send_file(gif_bytes, mimetype="image/gif")


if __name__ == '__main__':
    app.run(host="127.0.0.1", port=5000)
