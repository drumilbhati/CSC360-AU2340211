# Reflection — 3 November 2026

## Topics covered

- RGB color channels and pixel composition
- RGBA model and alpha transparency
- 32-bit integer (`int32`) pixel packing
- In-memory channel ordering (RGBA, ARGB, BGRA, ABGR)
- Byte ordering: Little Endian vs. Big Endian

---

## 1. How is RGB arranged in an image?

In digital imagery, **RGB** (Red, Green, Blue) represents colors as an additive combination of three primary light channels. Every individual pixel is defined by a triad of intensity values:

- **Red (R):** Intensity of the red component.
- **Green (G):** Intensity of the green component.
- **Blue (B):** Intensity of the blue component.

```text
Pixel (x, y) ───► [ Red Channel ] + [ Green Channel ] + [ Blue Channel ]
```

### Channel intensity ranges

In standard 24-bit true color images (8 bits per channel):
- Each channel value ranges from `0` to `255` ($2^8 = 256$ discrete levels).
- `0` indicates no light intensity (fully off / black).
- `255` indicates maximum light intensity (fully saturated).

#### Common color combinations:
- `(0, 0, 0)`: Pure Black
- `(255, 255, 255)`: Pure White
- `(255, 0, 0)`: Pure Red
- `(0, 255, 0)`: Pure Green
- `(0, 0, 255)`: Pure Blue
- `(255, 255, 0)`: Pure Yellow (Red + Green)

---

## 2. How is RGBA arranged in an image?

**RGBA** extends the RGB color model by incorporating an **Alpha ($\alpha$)** channel that controls opacity and transparency.

Each pixel is represented by four distinct components:
- **Red (R):** Color intensity (`0` to `255`)
- **Green (G):** Color intensity (`0` to `255`)
- **Blue (B):** Color intensity (`0` to `255`)
- **Alpha (A):** Opacity level (`0` to `255`)

```text
Alpha Value = 0   ───► Completely transparent (invisible)
Alpha Value = 128 ───► Semi-transparent (50% opacity)
Alpha Value = 255 ───► Completely opaque (solid color)
```

The alpha channel is essential for:
- Layering and compositing images over backgrounds.
- Alpha blending equations (e.g., Porter-Duff compositing).
- Anti-aliasing edges and rendering smooth translucent UI elements.

### 32-bit integer (`int32`) storage representation

An RGBA pixel is typically packed into a single **32-bit integer (`int32`)** where each of the 4 channels occupies **8 bits (1 byte)**:

```text
Total: 32 bits (4 bytes) per pixel

┌────────────────┬────────────────┬────────────────┬────────────────┐
│  Byte 3 (MSB)  │     Byte 2     │     Byte 1     │  Byte 0 (LSB)  │
│    [8 bits]    │    [8 bits]    │    [8 bits]    │    [8 bits]    │
└────────────────┴────────────────┴────────────────┴────────────────┘
```

This compact encoding allows $256^4 \approx 4.29 \text{ billion}$ distinct combinations of color and opacity levels, while fitting cleanly into standard 32-bit CPU registers.

---

## 3. How can these 4 bytes be ordered in memory?

Although every 32-bit pixel contains Red, Green, Blue, and Alpha channels, their byte sequence in memory varies depending on graphics frameworks, file formats, and GPU pipeline specifications.

```text
Memory Addresses:  Base + 0       Base + 1       Base + 2       Base + 3
                  ┌──────────────┬──────────────┬──────────────┬──────────────┐
  RGBA:           │     Red      │    Green     │     Blue     │    Alpha     │
                  ├──────────────┼──────────────┼──────────────┼──────────────┤
  ARGB:           │    Alpha     │     Red      │    Green     │     Blue     │
                  ├──────────────┼──────────────┼──────────────┼──────────────┤
  BGRA:           │     Blue     │    Green     │     Red      │    Alpha     │
                  ├──────────────┼──────────────┼──────────────┼──────────────┤
  ABGR:           │    Alpha     │     Blue     │    Green     │     Red      │
                  └──────────────┴──────────────┴──────────────┴──────────────┘
```

### Common channel arrangements

1. **RGBA:**
   - Order: `[Red, Green, Blue, Alpha]`
   - Standard format for WebGL, OpenGL, HTML5 Canvas (`ImageData`), and PNG specifications.
2. **ARGB:**
   - Order: `[Alpha, Red, Green, Blue]`
   - Frequently used in desktop graphical toolkits (e.g., Java AWT/JavaFX `TYPE_INT_ARGB`, legacy Windows GDI+).
3. **BGRA:**
   - Order: `[Blue, Green, Red, Alpha]`
   - Native layout for DirectX pipelines, Apple Metal, and Cairo graphics engines on x86 architectures.
4. **ABGR:**
   - Order: `[Alpha, Blue, Green, Red]`
   - Found in specific hardware rendering pipelines and image buffers.

Knowing the exact memory arrangement is critical: treating a `BGRA` buffer as `RGBA` swaps the red and blue channels, causing colors to appear inverted or tinted blue/orange.

---

## 4. Endianness: Little Endian vs. Big Endian

**Endianness** refers to the byte order used by computer architecture to store multi-byte data types (such as 32-bit integers) in physical memory addresses.

When a 32-bit pixel integer like `0xAARRGGBB` is saved across 4 sequential byte addresses in memory:

```text
Given 32-bit hex word: 0x AA RR GG BB
                          │  │  │  │
    Most Significant Byte ┘  │  │  └─ Least Significant Byte
                             │  │
                   (MSB) ────┘  └──── (LSB)
```

### Memory layout comparison

| Memory Offset | Big Endian (MSB first) | Little Endian (LSB first) |
|---|---|---|
| **Address + 0 (Lowest address)** | `0xAA` (Alpha - MSB) | `0xBB` (Blue - LSB) |
| **Address + 1** | `0xRR` (Red) | `0xGG` (Green) |
| **Address + 2** | `0xGG` (Green) | `0xRR` (Red) |
| **Address + 3 (Highest address)** | `0xBB` (Blue - LSB) | `0xAA` (Alpha - MSB) |

### Key distinctions

- **Big Endian:**
  - Stores the **Most Significant Byte (MSB)** at the lowest memory address.
  - Matches human left-to-right reading order (also referred to as *network byte order*).
- **Little Endian:**
  - Stores the **Least Significant Byte (LSB)** at the lowest memory address.
  - Dominant in modern consumer CPU architectures (x86, x86-64, and default mode in ARM processors).
- **Practical implication for pixels:**
  - A 32-bit integer packed as `0xAARRGGBB` in code will actually reside in memory as the byte sequence `[BB, GG, RR, AA]` on Little Endian machines (effectively a `BGRA` byte sequence).

---

## Key takeaways

| Term | Structure | Core Purpose |
|---|---|---|
| **RGB** | 3 channels (24 bits) | Additive color model representing Red, Green, and Blue light intensities |
| **RGBA** | 4 channels (32 bits) | RGB color model extended with an Alpha transparency/opacity channel |
| **Int32 Packing** | 4 × 8-bit bytes | Efficient representation storing a complete pixel in a single 32-bit register |
| **Channel Ordering** | RGBA, ARGB, BGRA, ABGR | Dictates the physical sequence of color bytes in memory buffers |
| **Endianness** | Little vs. Big Endian | Determines whether the least or most significant byte is stored at the lowest address |
