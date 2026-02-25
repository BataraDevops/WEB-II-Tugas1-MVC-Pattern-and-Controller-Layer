# Spring MVC — Controller, Model & Thymeleaf

## Latihan 1: Membuat Web App dengan @Controller & Thymeleaf

### Eksperimen 1: @Controller vs @RestController
Buka `/test/view`  →  Menampilkan halaman HTML dengan teks "Ini dari @Controller" di dalam tag `<h1>`.  
Buka `/test/text`  → Menampilkan plain text langsung di browser: `Ini dari @Controller + @ResponseBody → text langsung`.  
Apa perbedaannya? → `/test/view` me-render template HTML, `/test/text` return string mentah tanpa rendering.  

***Kesimpulan:** @Controller tanpa @ResponseBody → return nama template. Dengan @ResponseBody → return data langsung).*

### Eksperimen 2: Apa Terjadi Jika Template Tidak Ada?
Apakah berhasil? **Tidak**  
HTTP Status code: **500**  
Error message: 
```
Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.

Tue Feb 24 22:37:54 WIB 2026
There was an unexpected error (type=Internal Server Error, status=500).
```

***Kesimpulan:** Jika Controller return nama view yang tidak ada, Spring akan mengembalikan error 500 karena Thymeleaf Template Resolver tidak bisa menemukan file HTML yang diminta di folder templates/, dan exception ini tidak di-handle sehingga naik jadi 500.*

### Eksperimen 3: Perbedaan @RequestParam dan @PathVariable
|  URL   |     Hasil di Halaman     |
|:------:|:------------------------:|
| `/greet` | Selamat Pagi, Mahasiswa! |
| `/greet?name=Budi` |   Selamat Pagi, Budi!    |
| `/greet?name=Budi&waktu=Siang` |   Selamat Siang, Budi!   |
| `/greet/Ani` |        Halo, Ani!        |
| `/greet/Ani/detail` |        Halo, Ani!        |
| `/greet/Ani/detail?lang=EN` |       Hello, Ani!        |

**Pertanyaan:**

- URL mana yang pakai `@RequestParam`? **`/greet`, `/greet?name=Budi`, `/greet?name=Budi&waktu=Siang`**
- URL mana yang pakai `@PathVariable`? **`/greet/Ani`**
- URL mana yang pakai keduanya? **`/greet/Ani/detail` dan `/greet/Ani/detail?lang=EN`**

### **Pertanyaan Refleksi**

1. **Apa perbedaan antara `@Controller` dan `@RestController`? Dalam kasus apa kamu pakai masing-masing?**  
= *@Controller return nama template HTML yang di-render Thymeleaf → dipakai untuk web app dengan tampilan halaman.
   @RestController return data langsung (String/JSON) → dipakai untuk REST API yang dikonsumsi frontend atau service lain.*  
2. **Perhatikan bahwa template `product/list.html` dipakai oleh 3 endpoint berbeda (list all, filter by category, search). Apa keuntungannya membuat template yang reusable seperti ini?**  
= *Kalau ada perubahan tampilan, cukup ubah satu file. Tidak ada duplikasi kode, tidak ada risiko inkonsistensi antar halaman.*  
3. **Kenapa Controller inject `ProductService` (bukan langsung akses data di ArrayList)? Apa yang terjadi kalau Controller langsung manage data?**  
= *Pemisahan tanggung jawab. Controller hanya mengatur alur request-response, bukan logika data. Kalau Controller langsung manage data, saat source data diganti (misal ke database), Controller ikut harus diubah — padahal itu bukan urusannya.*  
4. **Apa perbedaan `model.addAttribute("products", products)` dengan return `products` langsung seperti di `@RestController`?**  
= *model.addAttribute() menaruh data ke template untuk di-render jadi HTML. Return langsung mengirim data mentah (JSON/String) ke response body tanpa rendering.*  
5. **Jika kamu buka `http://localhost:8080/products/abc` (ID bukan angka), apa yang terjadi? Kenapa?**  
= *Spring throw MethodArgumentTypeMismatchException → HTTP 400 Bad Request. "abc" tidak bisa dikonversi ke tipe Long yang diharapkan @PathVariable Long id.*  
6. **Apa keuntungan pakai `@RequestMapping("/products")` di level class dibanding menulis full path di setiap `@GetMapping`?**  
= *Kalau prefix /products mau diubah, cukup ubah satu tempat. Tidak perlu edit setiap method satu per satu.*  
7. **Dalam lab ini, kata "Model" muncul dalam beberapa konteks berbeda. Sebutkan minimal 2 arti yang berbeda dan jelaskan perbedaannya.**  
    - Hint: perhatikan `Model` di parameter method Controller, folder `model/`, dan class `Product`.  
Jawaban :  
    - Model Layer → lapisan logika bisnis & data, direpresentasikan oleh ProductService dan Product.java (konsep arsitektur MVC)
    - Spring Model object → parameter Model model di Controller, dipakai untuk kirim data ke template via model.addAttribute() (objek teknis Spring)

---

## Latihan 2: Navigasi Multi-Halaman & Layout Sederhana

### Eksperimen 1: Fragment yang Tidak Ada
Apakah error? **YA**  
Error message:
``` 
Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.

Wed Feb 25 08:17:21 WIB 2026
There was an unexpected error (type=Internal Server Error, status=500).
```  
  
***Kesimpulan:** Jika nama fragment salah, Thymeleaf akan melempar exception saat rendering dan mengembalikan HTTP 500, karena fragment tidak ditemukan di file yang direferensikan.*  

### Eksperimen 2: Static Resource Path

CSS masih bekerja? **YA**  
Path yang salah :  
Apakah halaman error? **Tidak**  
Apakah CSS diterapkan? **Tidak**  

***Kesimpulan:** `th:href="@{}"` lebih baik karena otomatis menyesuaikan context path kalau app di-deploy di sub-path, bukan selalu di root /. Jika file CSS tidak ada, halaman tetap muncul tapi tanpa styling.*

### **Pertanyaan Refleksi**

1. **Apa keuntungan menggunakan Thymeleaf Fragment untuk navbar dan footer?**  
= *Kalau mau ubah navbar, cukup ubah satu file (layout.html) dan semua halaman langsung ikut berubah. Tanpa fragment, harus ubah satu-satu di setiap file HTML.*  
2. **Apa bedanya file di `static/` dan `templates/`? Kenapa CSS ada di `static/` bukan `templates/`?**  
= *`templates/` itu file yang diproses dulu sama Thymeleaf sebelum dikirim ke browser (ada variabel, logika, dll). `static/` itu file yang langsung dikirim apa adanya tanpa diproses. CSS tidak butuh diproses, jadi ya taruh di `static/`.*  
3. **Apa yang dimaksud dengan `th:replace` dan bagaimana bedanya dengan `th:insert`?**  
    - Hint: coba ganti `th:replace` jadi `th:insert` dan inspect element di browser  
= *`th:replace` → tag `<div>`-nya hilang, diganti langsung sama isi fragment. `th:insert` → tag `<div>`-nya tetap ada, isi fragment ditaruh di dalamnya. Kalau inspect element di browser, pakai `th:insert` akan ada satu `div` ekstra pembungkus yang tidak ada kalau pakai `th:replace`.*  
4. **Kenapa kita pakai `@{}` untuk URL di Thymeleaf, bukan langsung tulis path?**  
= *Kalau app di-deploy di sub-path (misal `/myapp`), `@{/products}` otomatis jadi `/myapp/products`. Kalau hardcode `/products`, langsung salah dan link tidak jalan.*  
5. **Perhatikan bahwa `ProductController` inject `ProductService` melalui Constructor Injection (konsep dari Week 3). Apa jadinya kalau Controller tidak pakai DI dan langsung `new ProductService()` di dalam Controller?**  
= *Spring nggak tau ada object itu, jadi fitur-fitur Spring seperti @Transactional tidak bakal jalan. Selain itu, setiap request bisa bikin instance ProductService baru, artinya data di ArrayList selalu reset.*  