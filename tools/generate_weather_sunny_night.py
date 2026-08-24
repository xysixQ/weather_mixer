from __future__ import annotations

import math
from pathlib import Path

from PIL import Image, ImageDraw, ImageFilter


SIZE = 256
FRAME_COUNT = 120
OUTPUT = Path("app/src/main/res/drawable-nodpi/weather_sunny_night_anim.webp")


def draw_diamond(draw: ImageDraw.ImageDraw, x: float, y: float, radius: float, fill: tuple[int, int, int, int]) -> None:
    draw.polygon(
        [
            (x, y - radius),
            (x + radius * 0.34, y - radius * 0.34),
            (x + radius, y),
            (x + radius * 0.34, y + radius * 0.34),
            (x, y + radius),
            (x - radius * 0.34, y + radius * 0.34),
            (x - radius, y),
            (x - radius * 0.34, y - radius * 0.34),
        ],
        fill=fill,
    )


def make_frame(index: int) -> Image.Image:
    phase = math.tau * index / FRAME_COUNT
    # A nearly transparent full-frame backing avoids WebP delta-frame rectangles.
    image = Image.new("RGBA", (SIZE, SIZE), (0, 0, 0, 1))
    center_x = 126 + math.sin(phase) * 1.8
    center_y = 128 + math.cos(phase) * 2.8

    glow = Image.new("RGBA", image.size, (0, 0, 0, 0))
    glow_draw = ImageDraw.Draw(glow)
    glow_radius = 66 + math.sin(phase * 2.0) * 2.0
    glow_draw.ellipse(
        (
            center_x - glow_radius,
            center_y - glow_radius,
            center_x + glow_radius,
            center_y + glow_radius,
        ),
        fill=(126, 184, 255, 42),
    )
    image.alpha_composite(glow.filter(ImageFilter.GaussianBlur(18)))

    moon_mask = Image.new("L", image.size, 0)
    mask_draw = ImageDraw.Draw(moon_mask)
    moon_radius = 53
    mask_draw.ellipse(
        (
            center_x - moon_radius,
            center_y - moon_radius,
            center_x + moon_radius,
            center_y + moon_radius,
        ),
        fill=255,
    )
    mask_draw.ellipse(
        (
            center_x - 4,
            center_y - moon_radius - 12,
            center_x + moon_radius * 1.55,
            center_y + moon_radius * 0.92,
        ),
        fill=0,
    )
    moon = Image.new("RGBA", image.size, (214, 232, 255, 255))
    moon.putalpha(moon_mask)
    image.alpha_composite(moon)

    star_layer = Image.new("RGBA", image.size, (0, 0, 0, 0))
    star_draw = ImageDraw.Draw(star_layer)
    stars = (
        (51, 64, 7.0, 0.0),
        (194, 58, 5.5, 1.3),
        (210, 142, 7.5, 2.4),
        (55, 177, 5.0, 3.7),
        (174, 202, 4.5, 4.8),
    )
    for x, y, radius, offset in stars:
        alpha = round(185 + 35 * math.sin(phase * 2.0 + offset))
        draw_diamond(star_draw, x, y, radius, (235, 244, 255, alpha))
    image.alpha_composite(star_layer)
    return image


def main() -> None:
    frames = [make_frame(index) for index in range(FRAME_COUNT)]
    OUTPUT.parent.mkdir(parents=True, exist_ok=True)
    frames[0].save(
        OUTPUT,
        format="WEBP",
        save_all=True,
        append_images=frames[1:],
        duration=8,
        loop=0,
        lossless=False,
        quality=90,
        method=0,
        kmin=1,
        kmax=1,
    )


if __name__ == "__main__":
    main()
