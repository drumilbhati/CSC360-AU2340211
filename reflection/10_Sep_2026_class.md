# Reflection — 10 September 2026

## Topics covered

- Streaming compared with downloading
- Streaming in graphics applications
- Server-side and client-side rendering
- Storing image files and metadata
- Platform abstraction in AWT

---

## What is the difference between streaming and downloading?

Streaming and downloading both transfer data from a remote system to a local device. Their main difference is how and when that data is consumed and retained.

### Streaming

Streaming delivers content progressively so that it can be consumed before the complete resource has arrived. A media player buffers a small amount of data, begins playback, and continues receiving later segments while the user watches or listens.

Examples include:

- Watching an online video
- Listening to an internet radio station
- Playing a cloud-rendered game
- Viewing a remote desktop session
- Receiving a live camera feed

Streaming may be **live**, where content is produced as it is transmitted, or **on demand**, where stored content is delivered in segments.

### Downloading

Downloading transfers a file to local storage so it can be used after the transfer is complete. Some applications can open a partially downloaded file, but the main purpose is to retain a local copy.

Examples include:

- Downloading a software installer
- Saving a photograph
- Downloading a film for offline viewing
- Retrieving a document from cloud storage

### Detailed comparison

| Aspect | Streaming | Downloading |
|---|---|---|
| Start time | Consumption can begin after a small buffer is available | Use often begins after enough or all data has arrived |
| Internet connection | Usually required throughout consumption | Required during transfer but not for later offline use |
| Local storage | Usually stores temporary buffers or cache data | Retains a complete local file |
| Seeking | May require requesting a different segment | Local seeking is usually immediate after download |
| Quality | May change dynamically with network conditions | File quality normally remains fixed |
| Repeated use | May transfer the content again unless cached | Reuses the local copy without another transfer |
| Live content | Supported | A complete live file does not yet exist to download |
| Ownership and access | Often controlled by a service or session | User normally has a local copy, subject to licensing and DRM |

### Data usage

Streaming does not inherently use more data than downloading. If the same encoded content is consumed once at the same quality, the transferred amount may be similar.

Data usage depends on:

- Resolution, frame rate, and compression
- Adaptive quality changes
- How much of the content is consumed
- Repeated playback
- Local and network caching
- Protocol overhead

Streaming can save data when a user watches only part of a resource. Downloading can save data when the same content is used repeatedly because the local file does not need to be transferred again.

### Buffering and adaptation

Streaming systems use a buffer to absorb short network interruptions. If data arrives more slowly than it is consumed and the buffer becomes empty, playback pauses.

Adaptive bitrate streaming divides content into small segments at several quality levels. The player estimates available bandwidth and device capacity, then requests an appropriate version of each segment. This can reduce interruptions but may cause visible changes in quality.

---

## Where is streaming used in graphics?

Streaming in graphics can mean transmitting finished visual output or loading graphical assets as they are needed.

### 1. Video streaming

Video platforms encode audio and frames, divide them into segments, and send the segments to a media player. Compression reduces bandwidth requirements, while adaptive bitrate techniques adjust quality according to current conditions.

### 2. Texture and model streaming

Games and interactive 3D applications may contain more assets than can fit in memory at one time. The application loads nearby or visible textures, models, terrain, and animation data while removing assets that are no longer needed.

This approach can:

- Reduce initial loading time
- Limit memory usage
- Support large or open environments
- Load higher-detail assets as the viewer approaches

Asset streaming can occur from local storage, a network server, or both.

### 3. Cloud gaming

In cloud gaming:

1. A remote server runs the game and renders its graphics.
2. The rendered frames are encoded as video.
3. Video and audio are streamed to the player's device.
4. Controller, keyboard, and mouse input are sent back to the server.

This reduces local hardware requirements, but the experience depends heavily on latency, bandwidth, jitter, and video quality. Google Stadia is a historical example of this model; NVIDIA GeForce NOW and similar services continue to use it.

### 4. Remote desktops and visualization

Remote desktop, CAD, medical imaging, scientific visualization, and simulation systems can render complex graphics on a powerful remote machine and stream the resulting view to a less powerful client.

This avoids transferring an entire large dataset and keeps sensitive source data on the remote system. The client sends interaction commands while the server returns updated images or geometry.

### 5. Virtual and augmented reality

VR and AR systems may stream high-resolution scenes, spatial data, or remotely rendered frames to a headset. These applications have strict latency requirements because delayed visual updates can reduce immersion and cause discomfort.

Techniques such as prediction, local reprojection, level of detail, and foveated rendering can help reduce latency and bandwidth usage.

### 6. Progressive image and geometry delivery

Large images, maps, point clouds, and 3D models can be transmitted at increasing levels of detail. A low-resolution representation appears first, followed by finer data as it becomes available.

This provides useful visual feedback without forcing the user to wait for the complete high-resolution resource.

---

## Server-side rendering and client-side rendering

### Server-side rendering (SSR)

With server-side rendering, the server generates HTML for each request and sends the rendered document to the browser. JavaScript may then **hydrate** the page by attaching event handlers and interactive behavior to the existing HTML.

### Client-side rendering (CSR)

With client-side rendering, the server initially sends an HTML shell and JavaScript files. The browser executes the JavaScript, retrieves required data, and creates the page content.

### When SSR can be better

SSR often provides advantages for public, content-focused pages:

- **Earlier visible content:** The browser receives useful HTML without first executing the entire application bundle.
- **Search indexing:** Crawlers can inspect page content directly, including crawlers with limited JavaScript execution.
- **Link previews:** Metadata and page content can be available immediately to social and messaging services.
- **Resilience:** Meaningful content may still be visible when JavaScript loads slowly or fails.
- **Server and CDN caching:** Rendered responses may be cached when content and authentication rules allow it.

### Costs and limitations of SSR

SSR is not automatically faster or better. It introduces tradeoffs:

- Rendering consumes server resources.
- Dynamic pages may increase time to first byte.
- The browser may display content before hydration makes it interactive.
- Hydration requires JavaScript and can duplicate work between the server and browser.
- Personalized pages can be difficult to cache safely.
- The architecture and deployment process may become more complex.

### When CSR can be better

CSR is often suitable for highly interactive applications used after authentication, such as dashboards, design tools, and administration interfaces.

Potential benefits include:

- Fast navigation after the initial application has loaded
- Rich local state and interaction
- Reduced server-side HTML rendering work
- Static hosting for the application shell
- Clear separation between a frontend application and data APIs

Its main disadvantages are the initial JavaScript download and execution cost, greater dependence on JavaScript, and possible indexing or link-preview limitations.

### Comparison

| Concern | SSR | CSR |
|---|---|---|
| Initial HTML | Contains rendered page content | Often contains an application shell |
| Initial JavaScript requirement | Content may appear before JavaScript; interaction may still require hydration | JavaScript normally creates the content |
| Server workload | Higher because the server renders HTML | Lower HTML-rendering workload; APIs still require server resources |
| First visit | Can show content earlier | Can be slower with a large JavaScript bundle |
| Later navigation | May require server requests, depending on architecture | Often fast after the application loads |
| SEO and previews | Generally straightforward | Requires crawler support or prerendering |
| Interactivity | Available after scripts attach or hydrate | Strong after initialization |
| Caching | HTML can sometimes be cached at the server or CDN | Static assets and API responses can be cached effectively |

Modern frameworks often use a hybrid strategy that combines SSR, static generation, client-side navigation, and selective hydration.

### Accessibility

SSR does not automatically make a page accessible, and CSR does not automatically make it inaccessible. Accessibility depends primarily on:

- Semantic HTML
- Keyboard support
- Focus management
- Labels and descriptions
- Sufficient contrast
- Correct notification of dynamic updates
- Testing with assistive technology

SSR can improve resilience by making content available before JavaScript runs. A client-rendered application can also be accessible when it manages semantics, focus, and dynamic updates correctly.

---

## How should images be stored?

Amazon S3 is commonly used to store images, but it is an **object storage service**, not a traditional relational or document database.

### Recommended pattern

A common architecture stores:

- The image bytes in object storage
- Image metadata in a database

The database record may contain:

- Object key or URL
- Original filename
- MIME type
- Width and height
- File size
- Owner or related record ID
- Upload timestamp
- Access permissions
- Checksum or content hash

For example:

```text
Database record
├── id: 42
├── object_key: images/users/42/profile.webp
├── media_type: image/webp
├── width: 512
├── height: 512
└── owner_id: 1001

Object storage
└── images/users/42/profile.webp → binary image data
```

### Why object storage is commonly used

Services such as Amazon S3, Google Cloud Storage, and Azure Blob Storage provide:

- High durability and scalability
- Storage of large binary objects
- Access control and encryption
- Lifecycle and retention policies
- Versioning options
- Integration with content delivery networks
- Lower cost than many database-based approaches for large files

The application may provide users with time-limited signed URLs rather than making private images public.

### Can a database store images directly?

Yes. Relational databases can store binary data in `BLOB` or binary columns, and some document databases support binary values. Direct database storage may be appropriate when:

- Images are small
- Strong transactional consistency is required
- The number of files is limited
- Backup and access patterns favor a single data store

For large-scale media systems, object storage plus database metadata is generally more practical. The correct choice depends on file size, traffic, consistency, security, backup, and cost requirements.

---

## Why is AWT called abstract?

AWT stands for **Abstract Window Toolkit**. The word “abstract” refers mainly to the platform-independent API that hides differences between native operating-system windowing systems.

A Java program works with classes such as:

```java
Frame
Button
TextField
Graphics
```

For many traditional AWT components, the toolkit connects those Java objects to native platform components through peer implementations. The operating system performs much of the actual display and interaction work. This is why AWT controls are often described as **heavyweight components**.

```text
Java application
      ↓
AWT platform-independent API
      ↓
Platform-specific toolkit and peers
      ↓
Native operating-system window system
```

This abstraction allows similar Java source code to run on different operating systems, while components may still inherit aspects of each platform's native appearance and behavior.

AWT is not called abstract merely because it contains abstract classes and interfaces. It also contains many concrete classes. The important idea is that its public API abstracts the platform-specific details of windows, input events, fonts, colors, graphics, and related desktop services.

### AWT compared with Swing

| AWT | Swing |
|---|---|
| Provides the core windowing, graphics, and event infrastructure | Builds on AWT |
| Many controls use native peers | Most components are lightweight and painted by Java |
| Includes classes such as `Frame` and `Button` | Includes classes such as `JFrame` and `JButton` |
| Native appearance can vary by platform | Supports pluggable look and feel |

Swing still relies on AWT for top-level windows, event handling, layouts, fonts, colors, and Java2D graphics.

---

## Key takeaways

- Streaming supports progressive consumption, while downloading normally retains a complete local file.
- Data usage depends on quality, duration, caching, and repeated consumption—not only on the transfer method.
- Graphics streaming can transmit rendered frames or progressively load graphical assets.
- SSR can improve initial content delivery and indexing, but it introduces server and hydration costs.
- CSR can be effective for highly interactive applications after the initial load.
- Accessibility depends on implementation quality rather than rendering strategy alone.
- Amazon S3 is object storage, not a database; databases commonly store image metadata.
- AWT provides a platform-independent abstraction over native desktop and graphics services.
