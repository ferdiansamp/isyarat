# Isyarat - Aplikasi Penerjemah Bahasa Isyarat BISINDO

Aplikasi Android untuk menjembatani komunikasi antara teman tuli dan lawan bicara melalui terjemahan bahasa isyarat BISINDO secara instan.

## Kelompok
**NKelompok:** [Kelompok 4]

| No | Nama | NIM | Peran |
|----|------|-----|-------|
| 1 | Fariz Rahman Syahida | H1D024008 | Training Model + Backend |
| 2 | Refa Hasanah | H1D024021 | UI/UX + Frontend (Kerangka Aplikasi) |
| 3 | Mohammad Ferdian S. | H1D024023 | Frontend (Kamera dan Teks ke Isyarat) |
| 4 | Kadhim Ahmad Barakbah | H1D024035 | Frontend (Kamus) |


## Latar Belakang Aplikasi
Komunikasi adalah hak dan kebutuhan dasar manusia, namun hambatan bahasa antara Teman Tuli dan masyarakat umum masih sering menimbulkan miskomunikasi, termasuk dalam mengakses fasilitas publik, pendidikan, dan pekerjaan. Juru Bahasa Isyarat (JBI) selama ini menjadi jembatan utama, tetapi jumlahnya terbatas dan belum merata, sehingga banyak orang terpaksa menulis di kertas atau aplikasi catatan ponsel, yang lambat dan memutus kewajaran interaksi. Untuk menjawab masalah ini, dikembangkanlah "Isyarat", aplikasi Android berbasis kecerdasan buatan yang berfungsi sebagai penerjemah kamera instan. Dengan teknologi deteksi objek, aplikasi ini menerjemahkan gerakan Bahasa Isyarat Indonesia (BISINDO) menjadi teks secara real-time, sekaligus menyediakan fitur teks berukuran besar di layar untuk membalas pesan dari masyarakat mendengar. Melalui performa yang responsif, "Isyarat" diharapkan dapat menembus batasan komunikasi dan memberi Teman Tuli kemandirian untuk berinteraksi secara spontan.

## Fitur Utama
- **Beranda:** Halaman utama yang menampilkan informasi aplikasi dan akses cepat ke seluruh fitur.
- **Isyarat ke Teks:** Menerjemahkan gerakan tangan BISINDO melalui kamera menjadi huruf atau kata di layar.
- **Teks ke Layar:** Memperbesar teks yang diketik agar jelas dibaca lawan bicara, dilengkapi pengaturan ukuran teks dan rotasi 180°.
- **Kamus:** Referensi isyarat BISINDO.


## Teknologi yang Digunakan
- Kotlin
- Jetpack Compose
- TensorFlow Lite (deteksi berjalan langsung di perangkat)
- Android Studio

## Cara Menjalankan Aplikasi
1. Clone repository ini.
```bash
   git clone [https://github.com/ferdiansamp/isyarat.git]
```
2. Buka project menggunakan Android Studio.
3. Tunggu proses Gradle sync selesai.
4. Hubungkan smartphone Android atau jalankan emulator.
5. Klik **Run**.

## Cara Penggunaan
1. Buka aplikasi dan pilih mode di halaman **Beranda**.
2. Untuk **Isyarat ke Teks**, tekan *Mulai Kamera* lalu peragakan isyarat di depan kamera.
3. Untuk **Teks ke Layar**, ketik pesan lalu tunjukkan layar ke lawan bicara.

## Demo
[https://youtu.be/ljMIiKY0VxA]

## Screenshot
| Beranda | Isyarat ke Teks | Teks ke Layar | Kamus |
|:-------:|:---------------:|:-------------:|:-----:|
| <img width="200" alt="beranda" src="https://github.com/user-attachments/assets/a07c8900-0655-44e0-832d-cbec69038ab3" /> | <img width="200" alt="isyarat ke teks" src="https://github.com/user-attachments/assets/55ea9320-851b-49d5-9670-da4f2e673ca3" /> | <img width="200" alt="teks ke layar" src="https://github.com/user-attachments/assets/2b769ab5-38c9-42c9-9783-d16e61c0804e" /> | <img width="200" alt="kamus" src="https://github.com/user-attachments/assets/4c982b84-3256-473c-a4fb-3ceec214d83e" /> |

